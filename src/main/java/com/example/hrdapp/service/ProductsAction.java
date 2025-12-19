package com.example.hrdapp.service;

import com.example.hrdapp.model.Product;
import com.example.hrdapp.model.User;
import com.example.hrdapp.repository.ProductRepository;

import java.util.List;

public class ProductsAction extends MenuAction {
    private final ProductRepository productRepository;

    public ProductsAction(User currentUser, ProductRepository productRepository) {
        super(currentUser);
        this.productRepository = productRepository;
    }

    @Override
    public void execute() {
        System.out.println("\n=== DATA PRODUK ===");
        List<Product> products = productRepository.findAll();
        if (products.isEmpty()) {
            System.out.println("Tidak ada data produk.");
        } else {
            System.out.printf("%-5s %-20s %-10s %-10s%n", "ID", "Nama", "Harga", "Stok");
            System.out.println("------------------------------------------------");
            for (Product p : products) {
                System.out.printf("%-5d %-20s %-10.2f %-10d%n",
                    p.getId(), p.getName(), p.getPrice(), p.getStock());
            }
        }
    }
}
