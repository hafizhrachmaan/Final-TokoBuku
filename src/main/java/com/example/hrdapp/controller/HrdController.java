package com.example.hrdapp.controller;

import com.example.hrdapp.model.Role;
import com.example.hrdapp.model.User;
import com.example.hrdapp.model.UserStatus;
import com.example.hrdapp.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@Controller
@RequestMapping("/hrd")
public class HrdController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public HrdController(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/dashboard")
    public String dashboard(Model model, Principal principal) {
        model.addAttribute("username", principal.getName());
        model.addAttribute("pendingEmployees", userRepository.findAllByStatus(UserStatus.PENDING));
        model.addAttribute("verifiedEmployees", userRepository.findAllByStatus(UserStatus.VERIFIED));
        model.addAttribute("allRoles", Role.values());
        return "hrd-dashboard";
    }

    @PostMapping("/addEmployee")
    public String addEmployee(@RequestParam String username,
                              @RequestParam String password,
                              @RequestParam Role role) {
        if (userRepository.findByUsername(username).isPresent()) {
            return "redirect:/hrd/dashboard?error=true";
        }
        User newUser = new User();
        newUser.setUsername(username);
        newUser.setPassword(passwordEncoder.encode(password));
        newUser.setRole(role);
        newUser.setStatus(UserStatus.VERIFIED);
        userRepository.save(newUser);
        return "redirect:/hrd/dashboard?success=true";
    }

    @PostMapping("/accept/{userId}")
    public String acceptUser(@PathVariable Long userId) {
        userRepository.findById(userId).ifPresent(user -> {
            user.setStatus(UserStatus.VERIFIED);
            userRepository.save(user);
        });
        return "redirect:/hrd/dashboard?accepted=true";
    }

    @PostMapping("/reject/{userId}")
    public String rejectUser(@PathVariable Long userId) {
        try {
            userRepository.findById(userId).ifPresent(userRepository::delete);
            return "redirect:/hrd/dashboard?rejected=true";
        } catch (Exception e) {
            return "redirect:/hrd/dashboard?error_delete=true";
        }
    }

    @PostMapping("/cut/{userId}")
    public String cutEmployee(@PathVariable Long userId) {
        try {
            // Mencari user berdasarkan ID, jika ada maka hapus permanen
            userRepository.findById(userId).ifPresent(user -> {
                userRepository.delete(user);
            });
            return "redirect:/hrd/dashboard?cut=true";
        } catch (Exception e) {
            // Jika gagal (karena relasi DB atau lainnya), kembali ke dashboard dengan error
            return "redirect:/hrd/dashboard?error_delete=true";
        }
    }

    @PostMapping("/verify/{userId}")
    public String verifyUser(@PathVariable Long userId) {
        return acceptUser(userId);
    }
}