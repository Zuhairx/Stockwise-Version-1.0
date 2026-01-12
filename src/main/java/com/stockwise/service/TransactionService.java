package com.stockwise.service;

import com.stockwise.model.Product;
import com.stockwise.model.Transaction;
import com.stockwise.repository.ProductRepository;
import com.stockwise.repository.TransactionRepository;

import java.util.List;

public class TransactionService {

    private final TransactionRepository transactionRepo = new TransactionRepository();
    private final ProductRepository productRepo = new ProductRepository();

    public List<Transaction> getAllTransactions() {
        return transactionRepo.findAll();
    }

    public boolean process(Product product, String type, int qty) {

        int stock = product.getStock();

        if (type.equals("OUT") && qty > stock) {
            return false;
        }

        int newStock = type.equals("IN")
                ? stock + qty
                : stock - qty;

        System.out.println("Processing transaction: product=" + product.getId() + ", type=" + type + ", qty=" + qty);
        transactionRepo.insert(product.getId(), type, qty);
        productRepo.updateStock(product.getId(), newStock);
        System.out.println("Transaction processed successfully");

        return true;
    }

    public void deleteTransaction(String id) {
        // Fetch the transaction to reverse the stock change
        Transaction transaction = transactionRepo.findById(id);
        if (transaction != null) {
            // Get the product and current stock
            Product product = productRepo.findById(transaction.getProductId());
            if (product != null) {
                int currentStock = product.getStock();
                int quantity = transaction.getQuantity();
                int newStock;

                // Reverse the stock change: if IN, subtract; if OUT, add back
                if ("IN".equals(transaction.getType())) {
                    newStock = currentStock - quantity;
                } else if ("OUT".equals(transaction.getType())) {
                    newStock = currentStock + quantity;
                } else {
                    // Invalid type, skip stock update
                    newStock = currentStock;
                }

                // Update the stock
                productRepo.updateStock(transaction.getProductId(), newStock);
            }
        }

        // Delete the transaction
        transactionRepo.delete(id);
    }

    public void deleteAllTransactions() {
        transactionRepo.deleteAll();
    }

}
