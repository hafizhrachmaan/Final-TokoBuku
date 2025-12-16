package com.example.hrdapp.controller;

import com.example.hrdapp.model.Role;
import com.example.hrdapp.model.User;
import com.example.hrdapp.model.UserStatus;
import com.example.hrdapp.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class WebController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public WebController(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/")
    public String home() {
        return "redirect:/login";
    }

    @GetMapping("/register")
    public String showRegistrationForm() {
        return "register";
    }

    @PostMapping("/register")
    public String registerUser(@RequestParam String username,
                               @RequestParam String password,
                               @RequestParam Role role,
                               @RequestParam String fullName,
                               @RequestParam String email,
                               @RequestParam String phone,
                               @RequestParam String address,
                               @RequestParam String lastEducation,
                               @RequestParam String workExperience) {
        if (userRepository.findByUsername(username).isPresent()) {
            return "redirect:/register?error=true";
        }

        User newUser = new User();
        newUser.setUsername(username);
        newUser.setPassword(passwordEncoder.encode(password));
        newUser.setRole(role);
        newUser.setStatus(UserStatus.PENDING);
        newUser.setFullName(fullName);
        newUser.setEmail(email);
        newUser.setPhone(phone);
        newUser.setAddress(address);
        newUser.setLastEducation(lastEducation);
        newUser.setWorkExperience(workExperience);
        userRepository.save(newUser);

        return "redirect:/register?success=true";
    }
}