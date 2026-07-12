package com.tokobuku.nitnot.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.dao.DataIntegrityViolationException;
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
        model.addAttribute("userRole", "stocker");
        model.addAttribute("pageTitle", "Manajemen Stok");
        model.addAttribute("products", productService.getAllProducts());
        return "stocker-dashboard";
    }

    @GetMapping("/product/edit/{id}")
    public String editProductPage(@PathVariable Long id, Model model, Principal principal) {
        model.addAttribute("username", principal.getName());
        model.addAttribute("userRole", "stocker");
        model.addAttribute("pageTitle", "Edit Produk");

        Optional<Product> productOpt = productService.findProductById(id);
        if (productOpt.isPresent()) {
            model.addAttribute("product", productOpt.get());
            return "edit-product";
        } else {
            return "redirect:/stocker/dashboard";
        }
    }

    @PostMapping("/product/add")
    public String addProduct(@ModelAttribute Product product, RedirectAttributes ra) {
        productService.addProduct(product);
        ra.addFlashAttribute("success", "Produk baru berhasil disimpan!");
        return "redirect:/stocker/dashboard";
    }

    @PostMapping("/product/edit/{id}")
    public String updateProduct(@PathVariable Long id,
                                @ModelAttribute Product product,
                                RedirectAttributes ra) {
        try {
            // Set ID dari URL ke objek produk untuk memastikan ini adalah operasi update
            product.setId(id);
            // Panggil metode save (dibungkus dalam addProduct) yang akan melakukan update
            productService.addProduct(product);
            ra.addFlashAttribute("success", "Data produk berhasil diubah!");
        } catch (Exception e) {
            ra.addFlashAttribute("error", "Gagal memperbarui produk: " + e.getMessage());
        }
        return "redirect:/stocker/dashboard";
    }

    @GetMapping("/product/delete/{id}")
    public String deleteProduct(@PathVariable Long id, RedirectAttributes ra) {
        try {
            productService.deleteProduct(id);
            ra.addFlashAttribute("success", "Produk berhasil dihapus!");
        } catch (DataIntegrityViolationException e) {
            ra.addFlashAttribute("error", "Gagal: Produk ini tidak dapat dihapus karena sudah tercatat dalam riwayat transaksi.");
        } catch (Exception e) {
            ra.addFlashAttribute("error", "Gagal menghapus produk: " + e.getMessage());
        }
        return "redirect:/stocker/dashboard";
    }
}