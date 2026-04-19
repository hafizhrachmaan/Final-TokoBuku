package com.tokobuku.nitnot.service;

import com.tokobuku.nitnot.dto.CartItem;
import com.tokobuku.nitnot.model.Product;
import com.tokobuku.nitnot.model.User;
import com.tokobuku.nitnot.model.Transaction;
import org.springframework.transaction.annotation.Transactional;

import java.util.Scanner;

public class KasirAction extends MenuAction {

    private final ProductService productService;
    private final TransactionService transactionService;
    private final Scanner scanner;
    private final ShoppingCart shoppingCart;

    public KasirAction(User currentUser, ProductService productService, TransactionService transactionService, Scanner scanner) {
        super(currentUser);
        this.productService = productService;
        this.transactionService = transactionService;
        this.scanner = scanner;
        this.shoppingCart = new ShoppingCart(); // Each cashier session gets a new cart
    }

    @Override
    public void execute() {
        while (true) {
            displayDashboard();

            System.out.println("\n--- Menu Sesi Kasir ---");
            System.out.println("1. Tambah Produk ke Keranjang");
            System.out.println("2. Hapus Produk dari Keranjang");
            System.out.println("3. Checkout");
            System.out.println("4. Kosongkan Keranjang");
            System.out.println("0. Kembali ke Menu Utama");
            System.out.print("Pilih opsi: ");
            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1":
                    enterAddToCartLoop();
                    break;
                case "2":
                    removeFromCart();
                    break;
                case "3":
                    checkout();
                    break;
                case "4":
                    clearCart();
                    break;
                case "0":
                    return;
                default:
                    System.out.println("Pilihan tidak valid.");
            }
        }
    }

    private void displayDashboard() {
        System.out.println("\n\n==================== SESI KASIR ====================");
        System.out.println("\n--- Produk Tersedia ---");
        System.out.printf("%-5s | %-30s | %-15s | %-10s%n", "ID", "Nama", "Harga", "Stok");
        System.out.println("----------------------------------------------------------------------");
        for (Product p : productService.getAllProducts()) {
            System.out.printf("%-5d | %-30s | Rp %-12.2f | %-10d%n", p.getId(), p.getName(), p.getPrice(), p.getStock());
        }
        System.out.println("----------------------------------------------------------------------");


        System.out.println("\n--- Keranjang Belanja ---");
        if (shoppingCart.getItems().isEmpty()) {
            System.out.println("Keranjang kosong.");
        } else {
            System.out.printf("%-5s | %-30s | %-10s | %-15s%n", "ID", "Nama", "Jumlah", "Subtotal");
            System.out.println("----------------------------------------------------------------------");
            for (CartItem item : shoppingCart.getItems()) {
                System.out.printf("%-5d | %-30s | %-10d | Rp %-12.2f%n",
                    item.getProduct().getId(), item.getProduct().getName(), item.getQuantity(), item.getSubtotal());
            }
            System.out.println("----------------------------------------------------------------------");
        }
        System.out.printf("Total Keranjang: Rp %.2f%n", shoppingCart.getTotal());
        System.out.println("======================================================");
    }

    private void enterAddToCartLoop() {
        while (true) {
            System.out.print("Masukkan ID produk (atau '0' untuk selesai): ");
            String input = scanner.nextLine().trim();

            if (input.equals("0")) {
                break;
            }

            try {
                Long productId = Long.parseLong(input);
                productService.findProductById(productId).ifPresentOrElse(
                    product -> {
                        shoppingCart.addProduct(product);
                        System.out.println(" -> '" + product.getName() + "' berhasil ditambahkan.");
                    },
                    () -> System.out.println(" -> Produk dengan ID " + productId + " tidak ditemukan.")
                );
            } catch (NumberFormatException e) {
                System.out.println(" -> ID Produk harus berupa angka.");
            }
        }
    }

    private void removeFromCart() {
        System.out.print("Masukkan ID produk untuk dihapus dari keranjang: ");
        String input = scanner.nextLine().trim();
        try {
            Long productId = Long.parseLong(input);
            shoppingCart.removeProduct(productId);
            System.out.println(" -> Produk dengan ID " + productId + " telah dihapus dari keranjang.");
        } catch (NumberFormatException e) {
            System.out.println(" -> ID Produk harus berupa angka.");
        }
    }

    @Transactional
    private void checkout() {
        if (shoppingCart.getItems().isEmpty()) {
            System.out.println("Tidak bisa checkout, keranjang kosong.");
            return;
        }
        try {
            Transaction transaction = transactionService.processCheckout(currentUser.getUsername(), shoppingCart);
            System.out.println("\n--- CHECKOUT BERHASIL ---");
            System.out.println("ID Transaksi: " + transaction.getId());
            System.out.println("Total Belanja: Rp " + transaction.getTotalPrice());
            System.out.println("Struk belanja akan dicetak (simulasi).");
            System.out.println("-------------------------");
        } catch (IllegalStateException e) {
            System.out.println("\n--- CHECKOUT GAGAL ---");
            System.out.println("Pesan: " + e.getMessage());
            System.out.println("----------------------");
        }
    }
    
    private void clearCart() {
        shoppingCart.clear();
        System.out.println("Keranjang belanja telah dikosongkan.");
    }
}
