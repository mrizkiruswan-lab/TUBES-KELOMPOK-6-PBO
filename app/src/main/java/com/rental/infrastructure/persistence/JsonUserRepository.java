package com.rental.infrastructure.persistence;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.rentalapp.domain.entity.User;
import com.rentalapp.domain.repository.UserRepository;

import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.file.*;
import java.util.*;

public class JsonUserRepository implements UserRepository {

    private static final String FILE_PATH = "data/users.json";
    private final Gson gson = new GsonBuilder().setPrettyPrinting().create();
    private final Type listType = new TypeToken<List<User>>() {}.getType();

    @Override
    public Optional<User> findByUsername(String username) {
        return readAll().stream()
                .filter(u -> u.getUsername().equals(username))
                .findFirst();
    }

    private List<User> readAll() {
        try {
            Path path = Paths.get(FILE_PATH);
            if (!Files.exists(path)) {
                seedDefaultUsers();
            }
            String json = Files.readString(Paths.get(FILE_PATH));
            if (json.isBlank()) return new ArrayList<>();
            List<User> list = gson.fromJson(json, listType);
            return list != null ? list : new ArrayList<>();
        } catch (IOException e) {
            System.err.println("[ERROR] Gagal membaca " + FILE_PATH + ": " + e.getMessage());
            return new ArrayList<>();
        }
    }

    private void seedDefaultUsers() {
        List<User> defaults = List.of(
                new User("admin123",  "admin123",  User.Role.ADMIN),
                new User("staff123",  "staff123",  User.Role.STAFF),
                new User("owner123",  "owner123",  User.Role.OWNER)
        );
        try {
            Path path = Paths.get(FILE_PATH);
            Files.createDirectories(path.getParent());
            Files.writeString(path, gson.toJson(defaults));
        } catch (IOException e) {
            System.err.println("[ERROR] Gagal membuat " + FILE_PATH + ": " + e.getMessage());
        }
    }
}
