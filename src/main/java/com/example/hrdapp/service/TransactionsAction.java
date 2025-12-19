package com.example.hrdapp.service;

import com.example.hrdapp.model.Role;
import com.example.hrdapp.model.Transaction;
import com.example.hrdapp.model.User;
import com.example.hrdapp.repository.TransactionRepository;

import java.util.List;

public class TransactionsAction extends MenuAction {
    private final TransactionRepository transactionRepository;

    public TransactionsAction(User currentUser, TransactionRepository transactionRepository) {
        super(currentUser);
        this.transactionRepository = transactionRepository;
    }

    @Override
    public void execute() {
        System.out.println("\n=== DATA TRANSAKSI ===");
        List<Transaction> transactions;
        if (currentUser.getRole().equals(Role.HRD)) {
            transactions = transactionRepository.findAllByOrderByTransactionDateDesc();
        } else if (currentUser.getRole().equals(Role.KASIR)) {
            transactions = transactionRepository.findByUser(currentUser);
        } else {
            System.out.println("Akses ditolak.");
            return;
        }
        if (transactions.isEmpty()) {
            System.out.println("Tidak ada data transaksi.");
        } else {
            System.out.printf("%-5s %-20s %-15s %-10s%n", "ID", "Tanggal", "Kasir", "Total");
            System.out.println("------------------------------------------------");
            for (Transaction t : transactions) {
                System.out.printf("%-5d %-20s %-15s %-10.2f%n",
                    t.getId(), t.getTransactionDate(), t.getUser().getUsername(), t.getTotalPrice());
            }
        }
    }
}
