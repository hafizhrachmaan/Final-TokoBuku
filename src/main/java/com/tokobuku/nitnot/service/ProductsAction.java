package com.tokobuku.nitnot.service;

import com.tokobuku.nitnot.model.Product;
import com.tokobuku.nitnot.model.Role;
import com.tokobuku.nitnot.model.User;

import java.util.List;
import java.util.Scanner;

public class ProductsAction extends MenuAction {
    private final ProductService productService;
    private final Scanner scanner;

    public ProductsAction(User currentUser, ProductService productService, Scanner scanner) {
        super(currentUser);
        this.productService = productService;
        this.scanner = scanner;
    }

    @Override
    public void execute() {
        while (true) {
            System.out.println("\n\n================== MANAJEMEN PRODUK ==================");
            showAllProducts();

            // Hanya Stocker yang bisa CUD
            if (currentUser.getRole() == Role.STOCKER) {
                System.out.println("\n--- Menu Manajemen ---");
                System.out.println("1. Tambah Produk");
                System.out.println("2. Update Produk");
                System.out.println("3. Hapus Produk");
            }
            System.out.println("0. Kembali ke Menu Utama");
            System.out.print("Pilih opsi: ");
            String choice = scanner.nextLine().trim();

            if (currentUser.getRole() == Role.STOCKER) {
                switch (choice) {
                    case "1":
                        addProduct();
                        break;
                    case "2":
                        updateProduct();
                        break;
                    case "3":
                        deleteProduct();
                        break;
                    case "0":
                        return;
                    default:
                        System.out.println(" -> Pilihan tidak valid.");
                        break;
                }
            } else {
                if ("0".equals(choice)) {
                    return;
                } else {
                    System.out.println(" -> Pilihan tidak valid.");
                }
            }
        }
    }

    private void showAllProducts() {
        System.out.println("\n--- Daftar Semua Produk ---");
        List<Product> products = productService.getAllProducts();
        if (products.isEmpty()) {
            System.out.println("Tidak ada data produk.");
        } else {
            System.out.printf("%-5s | %-30s | %-15s | %-10s%n", "ID", "Nama", "Harga", "Stok");
            System.out.println("----------------------------------------------------------------------");
            for (Product p : products) {
                System.out.printf("%-5d | %-30s | Rp %-12.2f | %-10d%n",
                    p.getId(), p.getName(), p.getPrice(), p.getStock());
            }
            System.out.println("----------------------------------------------------------------------");
        }
    }

    private void addProduct() {
        System.out.println("\n--- Tambah Produk Baru ---");
        try {
            System.out.print(" -> Nama Produk: ");
            String name = scanner.nextLine();
            System.out.print(" -> Deskripsi: ");
            String description = scanner.nextLine();
            System.out.print(" -> Harga: ");
            double price = Double.parseDouble(scanner.nextLine());
            System.out.print(" -> Stok: ");
            int stock = Integer.parseInt(scanner.nextLine());

            Product newProduct = new Product();
            newProduct.setName(name);
            newProduct.setDescription(description);
            newProduct.setPrice(price);
            newProduct.setStock(stock);

            productService.addProduct(newProduct);
            System.out.println("Produk baru berhasil ditambahkan!");
        } catch (NumberFormatException e) {
            System.out.println("Input tidak valid. Harga dan Stok harus berupa angka.");
        } catch (Exception e) {
            System.out.println("Gagal menambahkan produk: " + e.getMessage());
        }
    }

    private void updateProduct() {
        System.out.println("\n--- Update Produk ---");
        try {
            System.out.print(" -> Masukkan ID produk yang akan diupdate: ");
            Long id = Long.parseLong(scanner.nextLine());

            productService.findProductById(id)
                .orElseThrow(() -> new IllegalArgumentException("Produk dengan ID " + id + " tidak ditemukan."));

            System.out.print(" -> Nama Produk baru: ");
            String name = scanner.nextLine();
            System.out.print(" -> Deskripsi baru: ");
            String description = scanner.nextLine();
            System.out.print(" -> Harga baru: ");
            double price = Double.parseDouble(scanner.nextLine());
            System.out.print(" -> Stok baru: ");
            int stock = Integer.parseInt(scanner.nextLine());

            productService.updateProduct(id, name, description, price, stock);
            System.out.println("Produk berhasil diupdate!");
        } catch (NumberFormatException e) {
            System.out.println("Input tidak valid. ID, Harga, dan Stok harus berupa angka.");
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        } catch (Exception e) {
            System.out.println("Gagal mengupdate produk: " + e.getMessage());
        }
    }

    private void deleteProduct() {
        System.out.println("\n--- Hapus Produk ---");
        try {
            System.out.print(" -> Masukkan ID produk yang akan dihapus: ");
            Long id = Long.parseLong(scanner.nextLine());
            productService.deleteProduct(id);
            System.out.println("Produk berhasil dihapus!");
        } catch (NumberFormatException e) {
            System.out.println("Input tidak valid. ID harus berupa angka.");
        } catch (Exception e) {
            System.out.println("Gagal menghapus produk: " + e.getMessage());
        }
    }
}
