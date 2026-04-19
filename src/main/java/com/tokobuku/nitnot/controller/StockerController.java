package com.tokobuku.nitnot.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.tokobuku.nitnot.model.Product;
import com.tokobuku.nitnot.service.ProductService;

import java.security.Principal;
import java.util.Optional;

@Controller
@RequestMapping("/stocker")
public class StockerController {

    private final ProductService productService;

    public StockerController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/dashboard")
    public String dashboard(Model model, Principal principal) {
        model.addAttribute("username", principal.getName());
        model.addAttribute("products", productService.getAllProducts());
        return "stocker-dashboard";
    }

    @GetMapping("/product/edit/{id}")
    public String editProductPage(@PathVariable Long id, Model model, Principal principal) {
        model.addAttribute("username", principal.getName());
        Optional<Product> productOpt = productService.findProductById(id);
        if (productOpt.isPresent()) {
            model.addAttribute("product", productOpt.get());
            return "edit-product";
        } else {
            // Optionally, add a flash attribute to show an error message
            return "redirect:/stocker/dashboard";
        }
    }

    @PostMapping("/product/add")
    public String addProduct(@ModelAttribute Product product, RedirectAttributes ra) {
        productService.addProduct(product);
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
        try {
            productService.updateProduct(id, name, description, price, stock);
            ra.addFlashAttribute("success", "Data produk diperbarui!");
        } catch (IllegalArgumentException e) {
            ra.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/stocker/dashboard";
    }

    @GetMapping("/product/delete/{id}")
    public String deleteProduct(@PathVariable Long id, RedirectAttributes ra) {
        try {
            productService.deleteProduct(id);
            ra.addFlashAttribute("success", "Produk berhasil dihapus!");
        } catch (Exception e) {
            ra.addFlashAttribute("error", "Gagal hapus! " + e.getMessage());
        }
        return "redirect:/stocker/dashboard";
    }
}