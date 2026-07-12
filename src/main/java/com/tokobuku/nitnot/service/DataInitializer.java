package com.tokobuku.nitnot.service;

import com.tokobuku.nitnot.model.Role;
import com.tokobuku.nitnot.model.User;
import com.tokobuku.nitnot.model.UserStatus;
import com.tokobuku.nitnot.repository.UserRepository;
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
        // Create 'hrd' user only if it doesn't exist. This is a safer pattern.
        if (userRepository.findByUsername("hrd").isEmpty()) {
            User newHrdUser = new User();
            newHrdUser.setUsername("hrd");
            newHrdUser.setPassword(passwordEncoder.encode("123"));
            newHrdUser.setRole(Role.HRD);
            newHrdUser.setStatus(UserStatus.VERIFIED);
            userRepository.save(newHrdUser);
        }
    }
}
