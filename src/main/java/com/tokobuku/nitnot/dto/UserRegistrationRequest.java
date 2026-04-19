package com.tokobuku.nitnot.dto;

import com.tokobuku.nitnot.model.Role;

public class UserRegistrationRequest {
    private final String username;
    private final String password;
    private final Role role;
    private final String fullName;
    private final String email;
    private final String phone;
    private final String address;
    private final String lastEducation;
    private final String workExperience;

    public UserRegistrationRequest(String username, String password, Role role, String fullName, String email, String phone, String address, String lastEducation, String workExperience) {
        this.username = username;
        this.password = password;
        this.role = role;
        this.fullName = fullName;
        this.email = email;
        this.phone = phone;
        this.address = address;
        this.lastEducation = lastEducation;
        this.workExperience = workExperience;
    }

    // Getters for all fields
    public String getUsername() { return username; }
    public String getPassword() { return password; }
    public Role getRole() { return role; }
    public String getFullName() { return fullName; }
    public String getEmail() { return email; }
    public String getPhone() { return phone; }
    public String getAddress() { return address; }
    public String getLastEducation() { return lastEducation; }
    public String getWorkExperience() { return workExperience; }
}
