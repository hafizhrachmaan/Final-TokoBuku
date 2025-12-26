package com.tokobuku.nitnot.service;

import com.tokobuku.nitnot.model.Role;
import com.tokobuku.nitnot.model.User;
import com.tokobuku.nitnot.repository.TransactionRepository;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Scanner;

public class MenuManager {
    private final User currentUser;
    private final Scanner scanner;
    // Using LinkedHashMap to preserve insertion order for dynamic menu display
    private final Map<String, MenuAction> actions;
    private final Map<String, String> menuDescriptions;

    public MenuManager(User currentUser, Scanner scanner, UserService userService, ProductService productService, TransactionService transactionService, TransactionRepository transactionRepository) {
        this.currentUser = currentUser;
        this.scanner = scanner;
        this.actions = new LinkedHashMap<>();
        this.menuDescriptions = new LinkedHashMap<>();
        initializeActions(userService, productService, transactionService, transactionRepository);
    }

    private void initializeActions(UserService userService, ProductService productService, TransactionService transactionService, TransactionRepository transactionRepository) {
        // Common action for all roles
        actions.put("1", new ProfileAction(currentUser));
        menuDescriptions.put("1", "Lihat Profil");

        // Role-specific actions
        switch (currentUser.getRole()) {
            case HRD:
                actions.put("2", new HrdUserManagementAction(currentUser, userService, scanner)); // New HRD user management action
                menuDescriptions.put("2", "Manajemen Pengguna HRD");
                actions.put("3", new ProductsAction(currentUser, productService, scanner));
                menuDescriptions.put("3", "Lihat Data Produk (Read-Only)");
                actions.put("4", new TransactionsAction(currentUser, transactionRepository));
                menuDescriptions.put("4", "Lihat Semua Transaksi");
                break;
            case STOCKER:
                actions.put("2", new ProductsAction(currentUser, productService, scanner));
                menuDescriptions.put("2", "Manajemen Produk (CRUD)");
                break;
            case KASIR:
                actions.put("2", new KasirAction(currentUser, productService, transactionService, scanner));
                menuDescriptions.put("2", "Mulai Sesi Kasir");
                actions.put("3", new TransactionsAction(currentUser, transactionRepository));
                menuDescriptions.put("3", "Lihat Riwayat Transaksi Saya");
                break;
        }
    }

    public void showMainMenu() {
        while (true) {
            System.out.println("\n\n==================== MENU UTAMA ====================");
            System.out.println("User: " + currentUser.getUsername() + " | Role: " + currentUser.getRole());
            System.out.println("----------------------------------------------------");
            
            // Dynamically display the menu based on available actions
            for (String key : menuDescriptions.keySet()) {
                System.out.println(key + ". " + menuDescriptions.get(key));
            }
            System.out.println("0. Keluar");

            System.out.print("\nPilih menu: ");
            String choice = scanner.nextLine().trim();

            if ("0".equals(choice)) {
                System.out.println("\nLogout berhasil. Anda akan kembali ke menu awal.");
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
