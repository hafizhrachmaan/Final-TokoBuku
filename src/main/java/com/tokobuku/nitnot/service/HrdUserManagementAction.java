package com.tokobuku.nitnot.service;

import com.tokobuku.nitnot.model.Role;
import com.tokobuku.nitnot.model.User;
import com.tokobuku.nitnot.model.UserStatus;

import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

public class HrdUserManagementAction extends MenuAction {
    private final UserService userService;
    private final Scanner scanner;

    public HrdUserManagementAction(User currentUser, UserService userService, Scanner scanner) {
        super(currentUser);
        this.userService = userService;
        this.scanner = scanner;
    }

    @Override
    public void execute() {
        if (!currentUser.getRole().equals(Role.HRD)) {
            System.out.println("Akses ditolak. Hanya HRD yang dapat mengelola pengguna.");
            return;
        }

        while (true) {
            System.out.println("\n\n================== MANAJEMEN PENGGUNA HRD ==================");
            displayUsers();

            System.out.println("\n--- Opsi Manajemen Pengguna ---");
            System.out.println("1. Tambah Karyawan Baru");
            System.out.println("2. Verifikasi/Terima Lamaran Pengguna");
            System.out.println("3. Tolak Lamaran Pengguna (Hapus)");
            System.out.println("4. Pecat Karyawan (Hapus Permanen)");
            System.out.println("0. Kembali ke Menu Utama");
            System.out.print("Pilih opsi: ");
            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1":
                    addEmployee();
                    break;
                case "2":
                    acceptUser();
                    break;
                case "3":
                    rejectUser();
                    break;
                case "4":
                    cutEmployee();
                    break;
                case "0":
                    return;
                default:
                    System.out.println("Pilihan tidak valid.");
            }
        }
    }

    private void displayUsers() {
        System.out.println("\n--- Pengguna Menunggu Persetujuan (PENDING) ---");
        List<User> pendingUsers = userService.getPendingEmployees();
        if (pendingUsers.isEmpty()) {
            System.out.println("Tidak ada pengguna yang menunggu persetujuan.");
        } else {
            printUserTable(pendingUsers);
        }

        System.out.println("\n--- Pengguna Terverifikasi (VERIFIED) ---");
        List<User> verifiedUsers = userService.getVerifiedEmployees();
        if (verifiedUsers.isEmpty()) {
            System.out.println("Tidak ada pengguna yang sudah terverifikasi.");
        } else {
            printUserTable(verifiedUsers);
        }
    }

    private void printUserTable(List<User> users) {
        System.out.printf("%-5s | %-25s | %-15s | %-10s | %-10s%n", "ID", "Nama Lengkap", "Username", "Role", "Status");
        System.out.println("--------------------------------------------------------------------------------");
        for (User user : users) {
            System.out.printf("%-5d | %-25s | %-15s | %-10s | %-10s%n",
                user.getId(), user.getFullName(), user.getUsername(), user.getRole(), user.getStatus());
        }
        System.out.println("--------------------------------------------------------------------------------");
    }

    private void addEmployee() {
        System.out.println("\n--- Tambah Karyawan Baru ---");
        try {
            System.out.print(" -> Username: ");
            String username = scanner.nextLine();
            System.out.print(" -> Password: ");
            String password = scanner.nextLine();
            System.out.print(" -> Role (HRD, STOCKER, KASIR): ");
            Role role = Role.valueOf(scanner.nextLine().trim().toUpperCase());

            userService.addEmployee(username, password, role);
            System.out.println("Karyawan baru '" + username + "' berhasil ditambahkan dan diverifikasi.");
        } catch (IllegalArgumentException e) {
            System.out.println(" -> Error: " + e.getMessage() + ". Pastikan role valid.");
        } catch (IllegalStateException e) {
            System.out.println(" -> Error: " + e.getMessage());
        } catch (Exception e) {
            System.out.println(" -> Terjadi kesalahan: " + e.getMessage());
        }
    }

    private void acceptUser() {
        System.out.println("\n--- Verifikasi/Terima Lamaran Pengguna ---");
        try {
            System.out.print(" -> Masukkan ID Pengguna yang akan diverifikasi: ");
            Long userId = Long.parseLong(scanner.nextLine());
            userService.acceptUser(userId)
                       .ifPresentOrElse(
                           user -> System.out.println("Pengguna '" + user.getUsername() + "' berhasil diverifikasi."),
                           () -> System.out.println("Pengguna dengan ID " + userId + " tidak ditemukan atau sudah diverifikasi.")
                       );
        } catch (NumberFormatException e) {
            System.out.println(" -> ID Pengguna harus berupa angka.");
        } catch (Exception e) {
            System.out.println(" -> Terjadi kesalahan: " + e.getMessage());
        }
    }

    private void rejectUser() {
        System.out.println("\n--- Tolak Lamaran Pengguna (Hapus) ---");
        try {
            System.out.print(" -> Masukkan ID Pengguna yang akan ditolak/dihapus: ");
            Long userId = Long.parseLong(scanner.nextLine());
            userService.rejectUser(userId);
            System.out.println("Pengguna dengan ID " + userId + " berhasil ditolak dan dihapus.");
        } catch (NumberFormatException e) {
            System.out.println(" -> ID Pengguna harus berupa angka.");
        } catch (Exception e) {
            System.out.println(" -> Terjadi kesalahan: " + e.getMessage());
        }
    }

    private void cutEmployee() {
        System.out.println("\n--- Pecat Karyawan (Hapus Permanen) ---");
        try {
            System.out.print(" -> Masukkan ID Karyawan yang akan dipecat: ");
            Long userId = Long.parseLong(scanner.nextLine());
            userService.cutEmployee(userId);
            System.out.println("Karyawan dengan ID " + userId + " berhasil dipecat dan dihapus permanen.");
        } catch (NumberFormatException e) {
            System.out.println(" -> ID Karyawan harus berupa angka.");
        } catch (Exception e) {
            System.out.println(" -> Terjadi kesalahan: " + e.getMessage());
        }
    }
}
