package com.example.hrdapp.service;

import com.example.hrdapp.model.User;
import com.example.hrdapp.repository.ProductRepository;
import com.example.hrdapp.repository.TransactionRepository;
import com.example.hrdapp.repository.UserRepository;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class MenuManager {
    private final User currentUser;
    private final Scanner scanner;
    private final Map<String, MenuAction> actions;

    public MenuManager(User currentUser, Scanner scanner, UserRepository userRepository, ProductRepository productRepository, TransactionRepository transactionRepository) {
        this.currentUser = currentUser;
        this.scanner = scanner;
        this.actions = new HashMap<>();
        initializeActions(userRepository, productRepository, transactionRepository);
    }

    private void initializeActions(UserRepository userRepository, ProductRepository productRepository, TransactionRepository transactionRepository) {
        actions.put("1", new ProfileAction(currentUser));
        actions.put("2", new UsersAction(currentUser, userRepository));
        actions.put("3", new ProductsAction(currentUser, productRepository));
        actions.put("4", new TransactionsAction(currentUser, transactionRepository));
    }

    public void showMainMenu() {
        while (true) {
            System.out.println("\n=== MENU UTAMA ===");
            System.out.println("Role: " + currentUser.getRole());
            System.out.println("1. Lihat Profil");
            System.out.println("2. Lihat Data Pengguna");
            System.out.println("3. Lihat Data Produk");
            System.out.println("4. Lihat Transaksi");
            System.out.println("0. Keluar");

            System.out.print("Pilih menu: ");
            String choice = scanner.nextLine().trim();

            if ("0".equals(choice)) {
                System.out.println("Terima kasih telah menggunakan HRD App CLI!");
                return;
            }

            MenuAction action = actions.get(choice);
            if (action != null) {
                action.execute();
            } else {
                System.out.println("Pilihan tidak valid.");
            }
        }
    }
}
