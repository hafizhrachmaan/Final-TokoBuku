package com.tokobuku.nitnot.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.tokobuku.nitnot.dto.UserRegistrationRequest;
import com.tokobuku.nitnot.model.Role;
import com.tokobuku.nitnot.service.UserService;

@Controller
public class WebController {

    private final UserService userService;

    public WebController(UserService userService) {
        this.userService = userService;
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

        UserRegistrationRequest request = new UserRegistrationRequest(username, password, role, fullName, email, phone, address, lastEducation, workExperience);

        try {
            userService.registerUser(request);
            return "redirect:/login?registrationSuccess=true";
        } catch (IllegalStateException e) {
            return "redirect:/register?error=true";
        }
    }
}