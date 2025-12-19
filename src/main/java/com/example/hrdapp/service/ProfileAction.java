package com.example.hrdapp.service;

import com.example.hrdapp.model.User;

public class ProfileAction extends MenuAction {

    public ProfileAction(User currentUser) {
        super(currentUser);
    }

    @Override
    public void execute() {
        System.out.println("\n=== PROFIL ANDA ===");
        System.out.println("Username: " + currentUser.getUsername());
        System.out.println("Role: " + currentUser.getRole());
        System.out.println("Status: " + currentUser.getStatus());
        System.out.println("Nama Lengkap: " + (currentUser.getFullName() != null ? currentUser.getFullName() : "Belum diisi"));
        System.out.println("Email: " + (currentUser.getEmail() != null ? currentUser.getEmail() : "Belum diisi"));
        System.out.println("Telepon: " + (currentUser.getPhone() != null ? currentUser.getPhone() : "Belum diisi"));
    }
}
