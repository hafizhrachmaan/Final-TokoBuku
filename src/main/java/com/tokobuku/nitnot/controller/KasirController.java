package com.tokobuku.nitnot.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.tokobuku.nitnot.model.Transaction;
import com.tokobuku.nitnot.repository.TransactionRepository;
import com.tokobuku.nitnot.service.PdfService;
import com.tokobuku.nitnot.service.ProductService;
import com.tokobuku.nitnot.service.ShoppingCart;
import com.tokobuku.nitnot.service.TransactionService;

import java.io.ByteArrayInputStream;
import java.security.Principal;

@Controller
@RequestMapping("/kasir")
public class KasirController {

    private final ProductService productService;
    private final TransactionService transactionService;
    private final TransactionRepository transactionRepository;
    private final PdfService pdfService;

    public KasirController(ProductService productService, TransactionService transactionService, TransactionRepository transactionRepository, PdfService pdfService) {
        this.productService = productService;
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
        model.addAttribute("products", productService.getAllProducts());
        // Cart items are now loaded via AJAX, but we pass an empty shell for initial page load
        model.addAttribute("cartItems", shoppingCart.getItems());
        model.addAttribute("cartTotal", shoppingCart.getTotal());
        return "kasir-dashboard";
    }

    // --- New AJAX API endpoints ---

    @PostMapping("/api/cart/add/{productId}")
    @ResponseBody
    public ShoppingCart handleAddToCartAjax(@PathVariable Long productId, HttpSession session) {
        ShoppingCart shoppingCart = getCartFromSession(session);
        productService.findProductById(productId).ifPresent(shoppingCart::addProduct);
        return shoppingCart;
    }

    @PostMapping("/api/cart/decrease/{productId}")
    @ResponseBody
    public ShoppingCart handleDecreaseCartAjax(@PathVariable Long productId, HttpSession session) {
        ShoppingCart shoppingCart = getCartFromSession(session);
        shoppingCart.decreaseProduct(productId);
        return shoppingCart;
    }

    @PostMapping("/api/cart/remove/{productId}")
    @ResponseBody
    public ShoppingCart handleRemoveFromCartAjax(@PathVariable Long productId, HttpSession session) {
        ShoppingCart shoppingCart = getCartFromSession(session);
        shoppingCart.removeProduct(productId);
        return shoppingCart;
    }

    @GetMapping("/api/cart")
    @ResponseBody
    public ShoppingCart getCartAjax(HttpSession session) {
        return getCartFromSession(session);
    }

    // --- Legacy and other endpoints ---

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
        Transaction transaction = transactionRepository.findByIdWithDetails(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid transaction Id:" + id));
        ByteArrayInputStream bis = pdfService.generateInvoicePdf(transaction);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "inline; filename=struk-" + id + ".pdf");
        return ResponseEntity.ok().headers(headers).contentType(MediaType.APPLICATION_PDF).body(new InputStreamResource(bis));
    }
}