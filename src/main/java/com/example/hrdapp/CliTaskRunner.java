package com.example.hrdapp;

import com.example.hrdapp.model.User;
import com.example.hrdapp.repository.ProductRepository;
import com.example.hrdapp.repository.TransactionRepository;
import com.example.hrdapp.repository.UserRepository;
import com.example.hrdapp.service.LoginService;
import com.example.hrdapp.service.MenuManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Scanner;

@Configuration
@ComponentScan(basePackages = "com.example.hrdapp")
public class CliTaskRunner implements CommandLineRunner {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        SpringApplication.run(CliTaskRunner.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        System.out.println("=========================================");
        System.out.println("      CLI TASK RUNNER - HRD APP");
        System.out.println("=========================================");

        LoginService loginService = new LoginService(userRepository, passwordEncoder, scanner);
        User currentUser = loginService.login();

        if (currentUser != null) {
            MenuManager menuManager = new MenuManager(currentUser, scanner, userRepository, productRepository, transactionRepository);
            menuManager.showMainMenu();
        } else {
            System.out.println("Login gagal. Keluar dari aplikasi.");
        }

        scanner.close();
    }
}
