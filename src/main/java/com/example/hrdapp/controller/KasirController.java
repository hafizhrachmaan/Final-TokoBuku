package com.example.hrdapp.controller;

import com.example.hrdapp.model.Transaction;
import com.example.hrdapp.repository.ProductRepository;
import com.example.hrdapp.repository.TransactionRepository;
import com.example.hrdapp.service.PdfService;
import com.example.hrdapp.service.ShoppingCart;
import com.example.hrdapp.service.TransactionService;
import jakarta.servlet.http.HttpSession;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayInputStream;
import java.security.Principal;

@Controller
@RequestMapping("/kasir")
public class KasirController {

    private final ProductRepository productRepository;
    private final TransactionService transactionService;
    private final TransactionRepository transactionRepository;
    private final PdfService pdfService;

    public KasirController(ProductRepository productRepository, TransactionService transactionService, TransactionRepository transactionRepository, PdfService pdfService) {
        this.productRepository = productRepository;
        this.transactionService = transactionService;
        this.transactionRepository = transactionRepository;
        this.pdfService = pdfService;
    }

    private ShoppingCart getCartFromSession(HttpSession session) {
        ShoppingCart cart = (ShoppingCart) session.getAttribute("shoppingCart");
        if (cart == null) {
            cart = new ShoppingCart();
            session.setAttribute("shoppingCart", cart);
        }
        return cart;
    }

    @GetMapping("/dashboard")
    public String dashboard(Model model, Principal principal, HttpSession session) {
        ShoppingCart shoppingCart = getCartFromSession(session);
        model.addAttribute("username", principal.getName());
        model.addAttribute("products", productRepository.findAll());
        model.addAttribute("cartItems", shoppingCart.getItems());
        model.addAttribute("cartTotal", shoppingCart.getTotal());
        return "kasir-dashboard";
    }

    // MODIFIKASI: Mendukung Update Fragment Tanpa Reload
    @PostMapping("/cart/add/{productId}")
    public String addToCart(@PathVariable Long productId, HttpSession session, Model model) {
        ShoppingCart shoppingCart = getCartFromSession(session);
        productRepository.findById(productId).ifPresent(shoppingCart::addProduct);
        
        // Menyiapkan data untuk fragment keranjang
        model.addAttribute("cartItems", shoppingCart.getItems());
        model.addAttribute("cartTotal", shoppingCart.getTotal());
        
        // Mengembalikan hanya bagian keranjang (fragment)
        return "kasir-dashboard :: cart-fragment";
    }

    // MODIFIKASI: Mendukung Hapus Fragment Tanpa Reload
    @PostMapping("/cart/remove/{productId}")
    public String removeFromCart(@PathVariable Long productId, HttpSession session, Model model) {
        ShoppingCart shoppingCart = getCartFromSession(session);
        shoppingCart.removeProduct(productId);
        
        // Menyiapkan data untuk fragment keranjang
        model.addAttribute("cartItems", shoppingCart.getItems());
        model.addAttribute("cartTotal", shoppingCart.getTotal());
        
        // Mengembalikan hanya bagian keranjang (fragment)
        return "kasir-dashboard :: cart-fragment";
    }

    @PostMapping("/cart/clear")
    public String clearCart(HttpSession session) {
        ShoppingCart shoppingCart = getCartFromSession(session);
        shoppingCart.clear();
        return "redirect:/kasir/dashboard";
    }

    @PostMapping("/checkout")
    public String checkout(Principal principal, HttpSession session) {
        ShoppingCart shoppingCart = getCartFromSession(session);
        try {
            Transaction transaction = transactionService.processCheckout(principal.getName(), shoppingCart);
            session.removeAttribute("shoppingCart");
            return "redirect:/kasir/history?checkout_success=" + transaction.getId();
        } catch (IllegalStateException e) {
            return "redirect:/kasir/dashboard?checkout_error=" + e.getMessage();
        }
    }

    @GetMapping("/history")
    public String history(Model model, Principal principal) {
        model.addAttribute("username", principal.getName());
        model.addAttribute("transactions", transactionRepository.findAllByOrderByTransactionDateDesc());
        return "transaction-history";
    }

    @GetMapping(value = "/transaction/pdf/{id}", produces = MediaType.APPLICATION_PDF_VALUE)
    public ResponseEntity<InputStreamResource> getTransactionPdf(@PathVariable Long id) {
        Transaction transaction = transactionRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid transaction Id:" + id));
        ByteArrayInputStream bis = pdfService.generateInvoicePdf(transaction);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "inline; filename=struk-" + id + ".pdf");
        return ResponseEntity.ok()
                .headers(headers)
                .contentType(MediaType.APPLICATION_PDF)
                .body(new InputStreamResource(bis));
    }
}