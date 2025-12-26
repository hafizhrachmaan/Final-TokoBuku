package com.tokobuku.nitnot.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.tokobuku.nitnot.model.Role;
import com.tokobuku.nitnot.repository.TransactionRepository;
import com.tokobuku.nitnot.service.ProductService;
import com.tokobuku.nitnot.service.UserService;

import java.security.Principal;

@Controller
@RequestMapping("/hrd")
public class HrdController {

    private final UserService userService;
    private final ProductService productService;
    private final TransactionRepository transactionRepository;

    public HrdController(UserService userService, ProductService productService, TransactionRepository transactionRepository) {
        this.userService = userService;
        this.productService = productService;
        this.transactionRepository = transactionRepository;
    }

    @GetMapping("/dashboard")
    public String dashboard(Model model, Principal principal) {
        model.addAttribute("username", principal.getName());
        model.addAttribute("pendingEmployees", userService.getPendingEmployees());
        model.addAttribute("verifiedEmployees", userService.getVerifiedEmployees());
        model.addAttribute("allRoles", Role.values());
        return "hrd-dashboard";
    }

    @GetMapping("/products")
    public String viewProducts(Model model, Principal principal) {
        model.addAttribute("username", principal.getName());
        model.addAttribute("allProducts", productService.getAllProducts());
        return "hrd-products";
    }

    @GetMapping("/transactions")
    public String viewTransactions(Model model, Principal principal) {
        model.addAttribute("username", principal.getName());
        model.addAttribute("allTransactions", transactionRepository.findAllByOrderByTransactionDateDesc());
        return "hrd-transactions";
    }


    @PostMapping("/addEmployee")
    public String addEmployee(@RequestParam String username,
                              @RequestParam String password,
                              @RequestParam Role role) {
        try {
            userService.addEmployee(username, password, role);
            return "redirect:/hrd/dashboard?success=true";
        } catch (IllegalStateException e) {
            return "redirect:/hrd/dashboard?error=true";
        }
    }

    @PostMapping("/accept/{userId}")
    public String acceptUser(@PathVariable Long userId) {
        userService.acceptUser(userId);
        return "redirect:/hrd/dashboard?accepted=true";
    }

    @PostMapping("/reject/{userId}")
    public String rejectUser(@PathVariable Long userId) {
        try {
            userService.rejectUser(userId);
            return "redirect:/hrd/dashboard?rejected=true";
        } catch (Exception e) {
            return "redirect:/hrd/dashboard?error_delete=true";
        }
    }

    @PostMapping("/cut/{userId}")
    public String cutEmployee(@PathVariable Long userId) {
        try {
            userService.cutEmployee(userId);
            return "redirect:/hrd/dashboard?cut=true";
        } catch (Exception e) {
            return "redirect:/hrd/dashboard?error_delete=true";
        }
    }

    @PostMapping("/verify/{userId}")
    public String verifyUser(@PathVariable Long userId) {
        // This endpoint is redundant, but we keep it and delegate to the same service method.
        return acceptUser(userId);
    }
}