package com.tokobuku.nitnot.service;

import com.tokobuku.nitnot.model.User;

public class ProfileAction extends MenuAction {

    public ProfileAction(User currentUser) {
        super(currentUser);
    }

    @Override
    public void execute() {
        System.out.println("\n\n==================== PROFIL SAYA ====================");
        System.out.printf("%-15s: %s%n", "Username", currentUser.getUsername());
        System.out.printf("%-15s: %s%n", "Nama Lengkap", (currentUser.getFullName() != null ? currentUser.getFullName() : "N/A"));
        System.out.printf("%-15s: %s%n", "Role", currentUser.getRole());
        System.out.printf("%-15s: %s%n", "Status", currentUser.getStatus());
        System.out.printf("%-15s: %s%n", "Email", (currentUser.getEmail() != null ? currentUser.getEmail() : "N/A"));
        System.out.printf("%-15s: %s%n", "Telepon", (currentUser.getPhone() != null ? currentUser.getPhone() : "N/A"));
        System.out.println("=====================================================");
    }
}
