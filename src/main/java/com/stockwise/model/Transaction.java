package com.stockwise.model;

import java.time.LocalDateTime;

public class Transaction {
    private String id;
    private String product_id;
    private String productName;
    private String type;
    private int quantity;
    private LocalDateTime date;

    public Transaction(String id, String product_id, String productName, String type, int quantity,
            LocalDateTime date) {
        this.id = id;
        this.product_id = product_id;
        this.productName = productName;
        this.type = type;
        this.quantity = quantity;
        this.date = date;
    }

    public String getId() {
        return id;
    }

    public String getProductId() {
        return product_id;
    }

    public String getProductName() {
        return productName;
    }

    public String getType() {
        return type;
    }

    public int getQuantity() {
        return quantity;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public String getFormattedId() {
        try {
            int numericId = Integer.parseInt(id);
            return String.format("TR-%03d", numericId);
        } catch (NumberFormatException e) {
            return id;
        }
    }
}
