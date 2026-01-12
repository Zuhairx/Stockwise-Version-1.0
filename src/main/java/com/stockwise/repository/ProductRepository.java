package com.stockwise.repository;

import com.stockwise.model.Product;
import com.stockwise.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProductRepository {

    public List<Product> findAll() {

        List<Product> products = new ArrayList<>();

        try (Connection conn = DBConnection.getConnection();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery("SELECT * FROM products")) {

            while (rs.next()) {
                products.add(new Product(
                        rs.getString("product_id"),
                        rs.getString("product_category"),
                        rs.getString("product_name"),
                        rs.getInt("price"),
                        rs.getInt("stock")));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return products;
    }

    public Product findById(String id) {
        String sql = "SELECT * FROM products WHERE product_id = ?";

        try (Connection conn = DBConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Product(
                            rs.getString("product_id"),
                            rs.getString("product_category"),
                            rs.getString("product_name"),
                            rs.getInt("price"),
                            rs.getInt("stock"));
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public Product findByName(String name) {
        String sql = "SELECT * FROM products WHERE product_name = ?";

        try (Connection conn = DBConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, name);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Product(
                            rs.getString("product_id"),
                            rs.getString("product_category"),
                            rs.getString("product_name"),
                            rs.getInt("price"),
                            rs.getInt("stock"));
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public void save(String id, String name, String category, int price) {
        String sql = "INSERT INTO products (product_id,  product_category, product_name, price) VALUES (?, ?, ?, ?)";

        try (Connection conn = DBConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, id);
            ps.setString(2, category);
            ps.setString(3, name);
            ps.setInt(4, price);
            ps.executeUpdate();
            conn.commit(); // Ensure commit

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void saveWithStock(String id, String name, String category, int price, int stock) {
        String sql = "INSERT INTO products (product_id, product_category, product_name, price, stock) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DBConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, id);
            ps.setString(2, category);
            ps.setString(3, name);
            ps.setInt(4, price);
            ps.setInt(5, stock);
            ps.executeUpdate();
            conn.commit(); // Ensure commit

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void delete(String id) {
        try (Connection conn = DBConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement("DELETE FROM products WHERE product_id=?")) {

            ps.setString(1, id);
            ps.executeUpdate();
            conn.commit(); // Ensure commit

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void update(String id, String name, String category, int price) {

        String sql = "UPDATE products SET product_name=?, product_category=?, price=? WHERE product_id=?";

        try (Connection conn = DBConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, name);
            ps.setString(2, category);
            ps.setInt(3, price);
            ps.setString(4, id);
            ps.executeUpdate();
            conn.commit(); // Ensure commit

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void updateStock(String productId, int newStock) {
        String sql = "UPDATE products SET stock=? WHERE product_id=?";

        try (Connection conn = DBConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, newStock);
            ps.setString(2, productId);
            ps.executeUpdate();
            conn.commit(); // Ensure commit

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateWithStock(String id, String name, String category, int price, int stock) {
        String sql = "UPDATE products SET product_name=?, product_category=?, price=?, stock=? WHERE product_id=?";

        try (Connection conn = DBConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, name);
            ps.setString(2, category);
            ps.setInt(3, price);
            ps.setInt(4, stock);
            ps.setString(5, id);
            ps.executeUpdate();
            conn.commit(); // Ensure commit

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void deleteAll() {
        String sql = "DELETE FROM products";

        try (Connection conn = DBConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.executeUpdate();
            conn.commit(); // Ensure commit

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public String getLastProductId() {
        String sql = "SELECT product_id FROM products ORDER BY product_id DESC LIMIT 1";

        try (Connection conn = DBConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()) {

            if (rs.next()) {
                return rs.getString("product_id");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

}
