package com.tokobuku.nitnot;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.tokobuku.nitnot.dto.UserRegistrationRequest;
import com.tokobuku.nitnot.model.Role;
import com.tokobuku.nitnot.model.User;
import com.tokobuku.nitnot.repository.TransactionRepository;
import com.tokobuku.nitnot.repository.UserRepository;
import com.tokobuku.nitnot.service.*;

import java.util.Arrays;
import java.util.Scanner;

@Configuration
@Profile("cli")
@ComponentScan(basePackages = "com.tokobuku.nitnot")
public class CliTaskRunner implements CommandLineRunner {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private ProductService productService;

    @Autowired
    private TransactionService transactionService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private ConfigurableApplicationContext context;


    private final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        SpringApplication.run(CliTaskRunner.class, args);
    }

    @Override
    public void run(String... args) {
        
        while (true) {
            clearConsole();
            System.out.println("\n\n=================================================");
            System.out.println("               NITNOT - Toko Buku          ");
            System.out.println("=================================================");
            System.out.println("\n  \"Pantau stok buku dan kelola transaksi harian");
            System.out.println("      dalam satu dashboard terintegrasi.\"");
            System.out.println("\n              © 2025 NITNOT SYSTEM");
            System.out.println("-------------------------------------------------");

            System.out.println("\n--- Portal Akses ---");
            System.out.println("1. Login");
            System.out.println("2. Daftar Karyawan Baru");
            System.out.println("0. Keluar");
            System.out.print("Pilih opsi: ");
            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1":
                    handleLogin();
                    // After logout, the loop will continue from here
                    break;
                case "2":
                    handleRegistration();
                    break;
                case "0":
                    System.out.println("\nTerima kasih, sampai jumpa!");
                    scanner.close();
                    // Use the proper Hafizh Boot exit method for a graceful shutdown
                    System.exit(SpringApplication.exit(context, () -> 0));
                    return;
                default:
                    System.out.println("Pilihan tidak valid.");
            }
        }
    }

    private void handleLogin() {
        LoginService loginService = new LoginService(userRepository, passwordEncoder, scanner);
        User currentUser = loginService.login();

        if (currentUser != null) {
            MenuManager menuManager = new MenuManager(currentUser, scanner, userService, productService, transactionService, transactionRepository);
            menuManager.showMainMenu();
        }
        // Do not return anything; let the run() loop continue
    }

    private void clearConsole() {
        try {
            final String os = System.getProperty("os.name");
            if (os.contains("Windows")) {
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
            } else {
                System.out.print("\033[H\033[2J");
                System.out.flush();
            }
        } catch (final Exception e) {
            //  Handle any exceptions.
        }
    }

    private void handleRegistration() {
        System.out.println("\n--- Daftar sebagai karyawan baru dan jadi bagian dari NITNOT ---");
        try {
            System.out.print(" -> Username: ");
            String username = scanner.nextLine();
            System.out.print(" -> Password: ");
            String password = scanner.nextLine();
            System.out.print(" -> Nama Lengkap: ");
            String fullName = scanner.nextLine();
            System.out.print(" -> Email: ");
            String email = scanner.nextLine();
            System.out.print(" -> Telepon: ");
            String phone = scanner.nextLine();
            System.out.print(" -> Alamat: ");
            String address = scanner.nextLine();
            System.out.print(" -> Pendidikan Terakhir: ");
            String lastEducation = scanner.nextLine();
            System.out.print(" -> Pengalaman Kerja: ");
            String workExperience = scanner.nextLine();

            Role role = null;
            while (role == null) {
                System.out.print(" -> Pilih Role Pendaftaran " + Arrays.toString(Role.values()) + ": ");
                String roleInput = scanner.nextLine().trim().toUpperCase();
                try {
                    role = Role.valueOf(roleInput);
                } catch (IllegalArgumentException e) {
                    System.out.println(" -> Role tidak valid. Silakan pilih dari daftar.");
                }
            }

            UserRegistrationRequest request = new UserRegistrationRequest(username, password, role, fullName, email, phone, address, lastEducation, workExperience);
            userService.registerUser(request);

            System.out.println("\nRegistrasi berhasil! Akun Anda telah dibuat sebagai '" + role + "' dengan status PENDING.");
            System.out.println("Silakan login setelah akun Anda disetujui oleh HRD.");

        } catch (IllegalStateException e) {
            System.out.println("\nRegistrasi Gagal: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("\nTerjadi error yang tidak diketahui saat registrasi: " + e.getMessage());
        }
    }
}

