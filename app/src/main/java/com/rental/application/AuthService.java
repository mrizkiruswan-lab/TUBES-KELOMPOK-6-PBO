package com.rental.application.service;

import com.rental.domain.entity.User;
import com.rental.domain.repository.UserRepository;

import java.util.Optional;

public class AuthService {

    private static final int MAX_ATTEMPTS = 3;

    private final UserRepository userRepository;
    private int failedAttempts = 0;

    public AuthService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User login(String username, String password) {
        if (failedAttempts >= MAX_ATTEMPTS) {
            throw new SecurityException("Sistem dikunci. Terlalu banyak percobaan login yang gagal.");
        }

        Optional<User> userOpt = userRepository.findByUsername(username);

        if (userOpt.isPresent() && userOpt.get().getPassword().equals(password)) {
            failedAttempts = 0; 
            return userOpt.get();
        }

        failedAttempts++;
        int remaining = MAX_ATTEMPTS - failedAttempts;

        if (remaining <= 0) {
            throw new SecurityException("Sistem dikunci. Terlalu banyak percobaan login yang gagal.");
        }

        throw new IllegalArgumentException(
                "Login Gagal! Username atau password salah. Sisa percobaan: " + remaining);
    }

    public void logout() {
        failedAttempts = 0;
    }

    public boolean isLocked() {
        return failedAttempts >= MAX_ATTEMPTS;
    }
}
