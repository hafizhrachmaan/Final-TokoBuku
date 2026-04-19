package com.tokobuku.nitnot.service;

import com.tokobuku.nitnot.model.Product;
import com.tokobuku.nitnot.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    /**
     * Retrieves all products.
     * @return A list of all products.
     */
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    /**
     * Adds a new product to the inventory.
     * @param product The product to add.
     * @return The saved product.
     */
    public Product addProduct(Product product) {
        // In a more complex scenario, validation logic would go here.
        return productRepository.save(product);
    }

    /**
     * Updates an existing product.
     * @param id The ID of the product to update.
     * @param name The new name.
     * @param description The new description.
     * @param price The new price.
     * @param stock The new stock quantity.
     * @return The updated product.
     * @throws IllegalArgumentException if the product is not found.
     */
    public Product updateProduct(Long id, String name, String description, double price, int stock) {
        Product p = productRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Product not found with id: " + id));
        p.setName(name);
        p.setDescription(description);
        p.setPrice(price);
        p.setStock(stock);
        return productRepository.save(p);
    }

    /**
     * Deletes a product by its ID.
     * @param id The ID of the product to delete.
     */
    public void deleteProduct(Long id) {
        if (!productRepository.existsById(id)) {
            throw new IllegalArgumentException("Product not found with id: " + id);
        }
        // In a real application, you might check for related entities first.
        productRepository.deleteById(id);
    }

    /**
     * Finds a product by its ID.
     * @param id The ID of the product.
     * @return An Optional containing the product if found.
     */
    public Optional<Product> findProductById(Long id) {
        return productRepository.findById(id);
    }
}
