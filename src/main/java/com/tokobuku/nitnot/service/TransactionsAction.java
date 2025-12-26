package com.tokobuku.nitnot.service;

import com.tokobuku.nitnot.model.Role;
import com.tokobuku.nitnot.model.Transaction;
import com.tokobuku.nitnot.model.User;
import com.tokobuku.nitnot.repository.TransactionRepository;

import java.time.format.DateTimeFormatter;
import java.util.List;

public class TransactionsAction extends MenuAction {
    private final TransactionRepository transactionRepository;
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");


    public TransactionsAction(User currentUser, TransactionRepository transactionRepository) {
        super(currentUser);
        this.transactionRepository = transactionRepository;
    }

    @Override
    public void execute() {
        System.out.println("\n\n================== RIWAYAT TRANSAKSI ==================");
        List<Transaction> transactions;

        if (currentUser.getRole().equals(Role.HRD)) {
            System.out.println("\n--- Menampilkan Semua Transaksi ---");
            transactions = transactionRepository.findAllByOrderByTransactionDateDesc();
        } else if (currentUser.getRole().equals(Role.KASIR)) {
            System.out.println("\n--- Menampilkan Transaksi oleh " + currentUser.getUsername() + " ---");
            transactions = transactionRepository.findByUser(currentUser);
        } else {
            System.out.println("Akses ditolak.");
            return;
        }

        if (transactions.isEmpty()) {
            System.out.println("Tidak ada data transaksi.");
        } else {
            System.out.printf("%-5s | %-20s | %-15s | %-15s%n", "ID", "Tanggal", "Kasir", "Total");
            System.out.println("-----------------------------------------------------------------");
            for (Transaction t : transactions) {
                System.out.printf("%-5d | %-20s | %-15s | Rp %-12.2f%n",
                    t.getId(),
                    t.getTransactionDate().format(FORMATTER),
                    t.getUser().getUsername(),
                    t.getTotalPrice());
            }
            System.out.println("-----------------------------------------------------------------");
        }
        System.out.println("=========================================================");
    }
}
