package com.tokobuku.nitnot.service;

import com.tokobuku.nitnot.model.User;

public abstract class MenuAction {
    protected User currentUser;

    public MenuAction(User currentUser) {
        this.currentUser = currentUser;
    }

    public abstract void execute();
}
