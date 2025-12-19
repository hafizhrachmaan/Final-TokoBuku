package com.example.hrdapp.controller;

import com.example.hrdapp.model.Product;
import com.example.hrdapp.repository.ProductRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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
        if (!model.containsAttribute("product")) {
            model.addAttribute("product", new Product());
        }
        return "stocker-dashboard";
    }

    @PostMapping("/product/add")
    public String addProduct(@ModelAttribute Product product, RedirectAttributes ra) {
        productRepository.save(product);
        ra.addFlashAttribute("success", "Produk baru berhasil disimpan!");
        return "redirect:/stocker/dashboard";
    }

    @PostMapping("/product/update")
    public String updateProduct(@RequestParam Long id,
                                @RequestParam String name,
                                @RequestParam String description,
                                @RequestParam double price,
                                @RequestParam int stock,
                                RedirectAttributes ra) {
        Product p = productRepository.findById(id).orElseThrow();
        p.setName(name);
        p.setDescription(description);
        p.setPrice(price);
        p.setStock(stock);
        productRepository.save(p);
        ra.addFlashAttribute("success", "Data produk diperbarui!");
        return "redirect:/stocker/dashboard";
    }

    @GetMapping("/product/delete/{id}")
    public String deleteProduct(@PathVariable Long id, RedirectAttributes ra) {
        try {
            productRepository.deleteById(id);
            ra.addFlashAttribute("success", "Produk berhasil dihapus!");
        } catch (Exception e) {
            ra.addFlashAttribute("error", "Hapus gagal! Data ini masih terikat dengan riwayat transaksi.");
        }
        return "redirect:/stocker/dashboard";
    }

    @GetMapping("/product/edit/{id}")
    public String showUpdateForm(@PathVariable("id") long id, Model model, Principal principal) {
        Product product = productRepository.findById(id).orElseThrow();
        model.addAttribute("productToEdit", product);
        model.addAttribute("username", principal.getName());
        return "edit-product";
    }
}