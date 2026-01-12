package com.stockwise.util;

import com.stockwise.model.Product;
import com.stockwise.model.Transaction;

import java.io.File;
import java.io.FileWriter;
import java.util.List;

public class CSVExporter {

    public static void exportTransactions(List<Transaction> list, File file) {
        try (FileWriter writer = new FileWriter(file)) {

            writer.write("ID,Product,Type,Qty,Date\n");

            for (Transaction t : list) {
                writer.write(String.format(
                        "%s,%s,%s,%d,%s\n",
                        t.getId(),
                        t.getProductName(),
                        t.getType(),
                        t.getQuantity(),
                        t.getDate()));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void exportProducts(List<Product> list, File file) {
        try (FileWriter writer = new FileWriter(file)) {

            writer.write("No,Product ID,Category,Product Name,Price,Stock\n");

            int no = 1;
            for (Product p : list) {
                writer.write(String.format(
                        "%d,%s,%s,%s,%d,%d\n",
                        no,
                        p.getId(),
                        p.getCategory(),
                        p.getName(),
                        p.getPrice(),
                        p.getStock()));
                no++;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
