package com.stockwise.service;

import com.stockwise.model.User;
import com.stockwise.repository.UserRepository;
import com.stockwise.util.PasswordUtil;

public class AuthService {

    private final UserRepository userRepository = new UserRepository();

    public User login(String username, String password) {

        User user = userRepository.findByUsername(username);

        if (user == null)
            return null;

        boolean valid = PasswordUtil.verifyPassword(password, user.getPasswordHash());

        return valid ? user : null;
    }

    public boolean register(String username, String password, String role) {
        if (userRepository.findByUsername(username) != null) {
            return false;
        }

        String hashedPassword = PasswordUtil.hashPassword(password);
        User newUser = new User(0, username, hashedPassword, role);

        return userRepository.save(newUser);
    }
}
