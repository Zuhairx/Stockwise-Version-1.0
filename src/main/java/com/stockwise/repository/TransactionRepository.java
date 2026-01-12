package com.stockwise.repository;

import com.stockwise.model.Transaction;
import com.stockwise.util.DBConnection;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class TransactionRepository {
    public List<Transaction> findAll() {
        List<Transaction> list = new ArrayList<>();

        System.out.println("Loading transactions from database...");

        try (Connection conn = DBConnection.getConnection();
                Statement st = conn.createStatement();
                ResultSet rs = st.executeQuery(
                        """
                                    SELECT t.transaction_id, p.product_id, p.product_name, t.type, t.quantity, t.transaction_date
                                    FROM transactions t
                                    JOIN products p ON t.product_id = p.product_id
                                    ORDER BY t.transaction_date DESC
                                """)) {

            while (rs.next()) {
                Transaction t = new Transaction(
                        rs.getString("transaction_id"),
                        rs.getString("product_id"),
                        rs.getString("product_name"),
                        rs.getString("type"),
                        rs.getInt("quantity"),
                        rs.getTimestamp("transaction_date").toLocalDateTime());
                list.add(t);
                System.out.println("Loaded transaction: " + t.getId() + ", product: " + t.getProductId());
            }

        } catch (Exception e) {
            System.out.println("Error loading transactions: " + e.getMessage());
            e.printStackTrace();
        }
        System.out.println("Total transactions loaded: " + list.size());
        return list;
    }

    public void insert(String productId, String type, int qty) {
        String transactionId = generateNextTransactionId();
        String sql = "INSERT INTO transactions (transaction_id, product_id, type, quantity, transaction_date) VALUES (?, ?, ?, ?, NOW())";

        try (Connection conn = DBConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, transactionId);
            ps.setString(2, productId);
            ps.setString(3, type);
            ps.setInt(4, qty);
            int rowsAffected = ps.executeUpdate();
            conn.commit();
            if (rowsAffected > 0) {
                System.out.println("Transaction inserted successfully: " + transactionId + ", " + productId + ", "
                        + type + ", " + qty);
            } else {
                System.out.println("Transaction insert failed: no rows affected");
            }

        } catch (SQLException e) {
            System.out.println("Error inserting transaction: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void insertWithDate(String productId, String type, int qty, LocalDateTime date) {
        String transactionId = generateNextTransactionId();
        String sql = "INSERT INTO transactions (transaction_id, product_id, type, quantity, transaction_date) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DBConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, transactionId);
            ps.setString(2, productId);
            ps.setString(3, type);
            ps.setInt(4, qty);
            ps.setTimestamp(5, Timestamp.valueOf(date));
            int rowsAffected = ps.executeUpdate();
            conn.commit();
            if (rowsAffected > 0) {
                System.out.println("Transaction inserted successfully: " + transactionId + ", " + productId + ", "
                        + type + ", " + qty + ", " + date);
            } else {
                System.out.println("Transaction insert failed: no rows affected");
            }

        } catch (SQLException e) {
            System.out.println("Error inserting transaction: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void insertOrUpdateWithId(String id, String productId, String type, int qty, LocalDateTime date) {
        String sql = "INSERT INTO transactions (transaction_id, product_id, type, quantity, transaction_date) VALUES (?, ?, ?, ?, ?) ON DUPLICATE KEY UPDATE product_id = VALUES(product_id), type = VALUES(type), quantity = VALUES(quantity), transaction_date = VALUES(transaction_date)";

        try (Connection conn = DBConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, id);
            ps.setString(2, productId);
            ps.setString(3, type);
            ps.setInt(4, qty);
            ps.setTimestamp(5, Timestamp.valueOf(date));
            int rowsAffected = ps.executeUpdate();
            conn.commit();
            if (rowsAffected > 0) {
                System.out.println("Transaction inserted or updated successfully: " + id + ", " + productId + ", "
                        + type + ", " + qty + ", " + date);
            } else {
                System.out.println("Transaction insert/update failed: no rows affected");
            }

        } catch (SQLException e) {
            System.out.println("Error inserting/updating transaction: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private String generateNextTransactionId() {
        String lastId = getLastTransactionId();
        if (lastId == null) {
            return "TR-001";
        }
        try {
            int numericId = Integer.parseInt(lastId.substring(3));
            return String.format("TR-%03d", numericId + 1);
        } catch (NumberFormatException e) {
            return "TR-001";
        }
    }

    public Transaction findById(String id) {
        String sql = """
                SELECT t.transaction_id, p.product_id, p.product_name, t.type, t.quantity, t.transaction_date
                FROM transactions t
                JOIN products p ON t.product_id = p.product_id
                WHERE t.transaction_id = ?
                """;

        try (Connection conn = DBConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Transaction(
                            rs.getString("transaction_id"),
                            rs.getString("product_id"),
                            rs.getString("product_name"),
                            rs.getString("type"),
                            rs.getInt("quantity"),
                            rs.getTimestamp("transaction_date").toLocalDateTime());
                }
            }

        } catch (SQLException e) {
            System.out.println("Error finding transaction: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    public void delete(String id) {
        try (Connection conn = DBConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement("DELETE FROM transactions WHERE transaction_id=?")) {

            ps.setString(1, id);
            ps.executeUpdate();
            conn.commit();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void deleteAll() {
        String sql = "DELETE FROM transactions";

        try (Connection conn = DBConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.executeUpdate();
            conn.commit();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public String getLastTransactionId() {
        String sql = "SELECT transaction_id FROM transactions ORDER BY transaction_id DESC LIMIT 1";

        try (Connection conn = DBConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()) {

            if (rs.next()) {
                return rs.getString("transaction_id");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
