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
        newUser.setStatus(UserStatus.VERIFIED); // HRD adds verified users directly
        userRepository.save(newUser);
        return "redirect:/hrd/dashboard?success=true";
    }

    @PostMapping("/verify/{userId}")
    public String verifyUser(@PathVariable Long userId) {
        userRepository.findById(userId).ifPresent(user -> {
            user.setStatus(UserStatus.VERIFIED);
            userRepository.save(user);
        });
        return "redirect:/hrd/dashboard";
    }
}
