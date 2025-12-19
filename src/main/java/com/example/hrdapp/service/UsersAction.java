package com.example.hrdapp.service;

import com.example.hrdapp.model.Role;
import com.example.hrdapp.model.User;
import com.example.hrdapp.repository.UserRepository;

import java.util.List;

public class UsersAction extends MenuAction {
    private final UserRepository userRepository;

    public UsersAction(User currentUser, UserRepository userRepository) {
        super(currentUser);
        this.userRepository = userRepository;
    }

    @Override
    public void execute() {
        if (!currentUser.getRole().equals(Role.HRD)) {
            System.out.println("Akses ditolak. Hanya HRD yang dapat melihat data pengguna.");
            return;
        }

        System.out.println("\n=== DATA PENGGUNA ===");
        List<User> users = userRepository.findAll();
        if (users.isEmpty()) {
            System.out.println("Tidak ada data pengguna.");
        } else {
            System.out.printf("%-5s %-15s %-10s %-10s%n", "ID", "Username", "Role", "Status");
            System.out.println("------------------------------------------------");
            for (User user : users) {
                System.out.printf("%-5d %-15s %-10s %-10s%n",
                    user.getId(), user.getUsername(), user.getRole(), user.getStatus());
            }
        }
    }
}
