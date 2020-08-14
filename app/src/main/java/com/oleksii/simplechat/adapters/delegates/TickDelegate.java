package com.oleksii.simplechat.adapters.delegates;

import com.oleksii.simplechat.models.User;

public interface TickDelegate {
    void checkUser(User user);
    void unCheckUser(User user);
}
