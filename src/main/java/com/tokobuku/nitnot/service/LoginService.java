package com.tokobuku.nitnot.service;

import com.tokobuku.nitnot.model.User;
import com.tokobuku.nitnot.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;
import java.util.Scanner;

public class LoginService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final Scanner scanner;

    public LoginService(UserRepository userRepository, PasswordEncoder passwordEncoder, Scanner scanner) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.scanner = scanner;
    }

    public User login() {
        int attempts = 0;
        final int MAX_ATTEMPTS = 3;

        while (attempts < MAX_ATTEMPTS) {
            System.out.println("\n=== LOGIN ===");
            System.out.print("Username: ");
            String username = scanner.nextLine().trim();

            System.out.print("Password: ");
            String password = scanner.nextLine().trim();

            Optional<User> userOpt = userRepository.findByUsername(username);
            if (userOpt.isPresent()) {
                User user = userOpt.get();
                if (passwordEncoder.matches(password, user.getPassword())) {
                    switch (user.getStatus()) {
                        case VERIFIED:
                            System.out.println("Login berhasil! Selamat datang, " + user.getUsername() + " (" + user.getRole() + ")");
                            return user;
                        case PENDING:
                            System.out.println("Login ditolak. Akun Anda sedang menunggu persetujuan HRD.");
                            break;
                        case INACTIVE:
                            System.out.println("Login ditolak. Akun Anda sudah tidak aktif.");
                            break;
                    }
                } else {
                    System.out.println("Password salah.");
                }
            } else {
                System.out.println("Username tidak ditemukan.");
            }

            attempts++;
            if (attempts < MAX_ATTEMPTS) {
                System.out.println("Percobaan login tersisa: " + (MAX_ATTEMPTS - attempts));
            }
        }

        System.out.println("Terlalu banyak percobaan login gagal.");
        return null;
    }
}
