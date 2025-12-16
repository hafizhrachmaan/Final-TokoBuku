package com.example.hrdapp.controller;

import com.example.hrdapp.model.Product;
import com.example.hrdapp.repository.ProductRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@Controller
@RequestMapping("/stocker")
public class StockerController {

    private final ProductRepository productRepository;

    public StockerController(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @GetMapping("/dashboard")
    public String dashboard(Model model, Principal principal) {
        model.addAttribute("username", principal.getName());
        model.addAttribute("products", productRepository.findAll());
        model.addAttribute("product", new Product()); // For the form
        return "stocker-dashboard";
    }

    @PostMapping("/product/add")
    public String addProduct(@ModelAttribute Product product) {
        productRepository.save(product);
        return "redirect:/stocker/dashboard";
    }

    @PostMapping("/product/update")
    public String updateProduct(@RequestParam Long id,
                                @RequestParam String name,
                                @RequestParam String description,
                                @RequestParam double price,
                                @RequestParam int stock) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid product Id:" + id));
        product.setName(name);
        product.setDescription(description);
        product.setPrice(price);
        product.setStock(stock);
        productRepository.save(product);
        return "redirect:/stocker/dashboard";
    }

    @GetMapping("/product/delete/{id}")
    public String deleteProduct(@PathVariable Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid product Id:" + id));
        productRepository.delete(product);
        return "redirect:/stocker/dashboard";
    }

    @GetMapping("/product/edit/{id}")
    public String showUpdateForm(@PathVariable("id") long id, Model model, Principal principal) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid product Id:" + id));
        model.addAttribute("productToEdit", product);
        model.addAttribute("username", principal.getName());
        return "edit-product";
    }
}