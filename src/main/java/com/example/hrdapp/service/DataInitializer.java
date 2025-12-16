package com.example.hrdapp.service;

import com.example.hrdapp.model.Role;
import com.example.hrdapp.model.User;
import com.example.hrdapp.model.UserStatus;
import com.example.hrdapp.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public DataInitializer(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) throws Exception {
        // Find and delete the old 'hrd' user if it exists
        Optional<User> oldHrdUser = userRepository.findByUsername("hrd");
        oldHrdUser.ifPresent(userRepository::delete);

        // Create a new 'hrd' user with the new password
        User newHrdUser = new User();
        newHrdUser.setUsername("hrd");
        newHrdUser.setPassword(passwordEncoder.encode("123"));
        newHrdUser.setRole(Role.HRD);
        newHrdUser.setStatus(UserStatus.VERIFIED);
        userRepository.save(newHrdUser);
    }
}
