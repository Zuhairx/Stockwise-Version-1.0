package com.stockwise.util;

import com.stockwise.model.Product;
import com.stockwise.model.Transaction;
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
            String line = br.readLine();
            while ((line = br.readLine()) != null) {
                String[] fields = line.split(",");
                if (fields.length >= 5) {
                    transactions.add(fields);
                }
            }

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

                    }
                }
                if (date == null) {
                    throw new Exception("Unable to parse date: " + fields[4].trim());
                }

                Product product = productRepo.findByName(productName);
                if (product != null) {

                    Transaction existingTransaction = repo.findById(id);
                    if (existingTransaction != null) {

                        if (existingTransaction.getQuantity() == qty && existingTransaction.getType().equals(type)) {
                            throw new Exception("Stock & Type is still the same for transaction ID: " + id);
                        }

                        int currentStock = product.getStock();
                        int revertStock = existingTransaction.getType().equals("IN")
                                ? currentStock - existingTransaction.getQuantity()
                                : currentStock + existingTransaction.getQuantity();
                        productRepo.updateStock(product.getId(), revertStock);

                        repo.insertOrUpdateWithId(id, product.getId(), type, qty, date);

                        currentStock = revertStock;
                        int newStock = type.equals("IN") ? currentStock + qty : currentStock - qty;
                        productRepo.updateStock(product.getId(), newStock);
                    } else {

                        repo.insertOrUpdateWithId(id, product.getId(), type, qty, date);

                        int currentStock = product.getStock();
                        int newStock = type.equals("IN") ? currentStock + qty : currentStock - qty;
                        productRepo.updateStock(product.getId(), newStock);
                    }
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
            String line = br.readLine();
            while ((line = br.readLine()) != null) {
                String[] fields = line.split(",");
                if (fields.length >= 6) {
                    String id = fields[1].trim();
                    String category = fields[2].trim();
                    String name = fields[3].trim();
                    int price = Integer.parseInt(fields[4].trim());
                    int stock = fields.length > 5 ? Integer.parseInt(fields[5].trim()) : 0;

                    Product p = new Product(id, category, name, price, stock);
                    products.add(p);
                }
            }

            ProductRepository repo = new ProductRepository();
            for (Product p : products) {
                Product existing = repo.findById(p.getId());
                if (existing != null) {

                    repo.updateWithStock(p.getId(), p.getName(), p.getCategory(), p.getPrice(), p.getStock());
                } else {

                    repo.saveWithStock(p.getId(), p.getName(), p.getCategory(), p.getPrice(), p.getStock());
                }
            }

        } catch (IOException | NumberFormatException e) {
            throw new Exception("Error importing CSV: " + e.getMessage(), e);
        }
    }
}
