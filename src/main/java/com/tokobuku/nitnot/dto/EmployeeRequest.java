package com.tokobuku.nitnot.dto;

import com.tokobuku.nitnot.model.Role;

public class EmployeeRequest {
    private String username;
    private String password;
    private Role role;

    // Getters and Setters
    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }

    public Role getRole() {
        return role;
    }
    public void setRole(Role role) {
        this.role = role;
    }
}
