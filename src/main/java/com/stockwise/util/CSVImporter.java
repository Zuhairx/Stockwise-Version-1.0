package com.stockwise.util;

import com.stockwise.model.Product;
import com.stockwise.repository.ProductRepository;
import com.stockwise.repository.TransactionRepository;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class CSVImporter {

    public static void importTransactions(String filePath) throws Exception {
        List<String[]> transactions = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line = br.readLine(); // Skip header
            while ((line = br.readLine()) != null) {
                String[] fields = line.split(",");
                if (fields.length >= 5) { // ID,Product,Type,Qty,Date
                    transactions.add(fields);
                }
            }

            // Insert into database
            TransactionRepository repo = new TransactionRepository();
            ProductRepository productRepo = new ProductRepository();
            DateTimeFormatter[] formatters = {
                DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss"),
                DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm"),
                DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"),
                DateTimeFormatter.ofPattern("yyyy-MM-dd'T' HH:mm")
            };

            for (String[] fields : transactions) {
                String id = fields[0].trim();
                String productName = fields[1].trim();
                String type = fields[2].trim();
                int qty = Integer.parseInt(fields[3].trim());
                LocalDateTime date = null;
                for (DateTimeFormatter formatter : formatters) {
                    try {
                        date = LocalDateTime.parse(fields[4].trim(), formatter);
                        break;
                    } catch (Exception e) {
                        // try next
                    }
                }
                if (date == null) {
                    throw new Exception("Unable to parse date: " + fields[4].trim());
                }

                // Find product by name
                Product product = productRepo.findByName(productName);
                if (product != null) {
                    repo.insertOrUpdateWithId(id, product.getId(), type, qty, date);
                    // Update product stock
                    int currentStock = product.getStock();
                    int newStock = type.equals("IN") ? currentStock + qty : currentStock - qty;
                    productRepo.updateStock(product.getId(), newStock);
                } else {
                    throw new Exception("Product not found: " + productName);
                }
            }

        } catch (IOException | NumberFormatException e) {
            throw new Exception("Error importing CSV: " + e.getMessage(), e);
        }
    }

    public static void importProducts(String filePath) throws Exception {
        List<Product> products = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line = br.readLine(); // Skip header
            while ((line = br.readLine()) != null) {
                String[] fields = line.split(",");
                if (fields.length >= 6) { // Now expecting 6 fields: No, ID, Category, Name, Price, Stock
                    String id = fields[1].trim();
                    String category = fields[2].trim();
                    String name = fields[3].trim();
                    int price = Integer.parseInt(fields[4].trim());
                    int stock = fields.length > 5 ? Integer.parseInt(fields[5].trim()) : 0;

                    Product p = new Product(id, category, name, price, stock);
                    products.add(p);
                }
            }

            // Insert or update into database
            ProductRepository repo = new ProductRepository();
            for (Product p : products) {
                Product existing = repo.findById(p.getId());
                if (existing != null) {
                    // Update existing product
                    repo.updateWithStock(p.getId(), p.getName(), p.getCategory(), p.getPrice(), p.getStock());
                } else {
                    // Insert new product
                    repo.saveWithStock(p.getId(), p.getName(), p.getCategory(), p.getPrice(), p.getStock());
                }
            }

        } catch (IOException | NumberFormatException e) {
            throw new Exception("Error importing CSV: " + e.getMessage(), e);
        }
    }
}
