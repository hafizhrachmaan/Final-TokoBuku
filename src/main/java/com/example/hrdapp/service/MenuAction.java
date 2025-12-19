package com.example.hrdapp.service;

import com.example.hrdapp.model.User;

public abstract class MenuAction {
    protected User currentUser;

    public MenuAction(User currentUser) {
        this.currentUser = currentUser;
    }

    public abstract void execute();
}
