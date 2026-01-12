package com.stockwise.service;

import com.stockwise.model.Product;
import com.stockwise.repository.ProductRepository;

import java.util.List;

public class ProductService {

    private final ProductRepository repository = new ProductRepository();

    public ProductService() {
        initializeIdCounter();
    }

    private void initializeIdCounter() {
        String lastId = repository.getLastProductId();
        if (lastId != null && lastId.startsWith("PR-")) {
            try {
                int lastNumber = Integer.parseInt(lastId.substring(3));
                Product.setNextId(lastNumber + 1);
            } catch (NumberFormatException e) {
            }
        }
    }

    public List<Product> getAllProducts() {
        return repository.findAll();
    }

    public void addProduct(String id, String name, String category, int price) {
        repository.save(id, name, category, price);
    }

    public void addProductWithStock(String id, String name, String category, int price, int stock) {
        repository.saveWithStock(id, name, category, price, stock);
    }

    public void deleteProduct(String id) {
        repository.delete(id);
    }

    public void updateProduct(String id, String name, String category, int price) {
        repository.update(id, name, category, price);
    }

    public void deleteAllProducts() {
        repository.deleteAll();
    }

    public List<Product> reloadProducts() {
        return getAllProducts();
    }

}
