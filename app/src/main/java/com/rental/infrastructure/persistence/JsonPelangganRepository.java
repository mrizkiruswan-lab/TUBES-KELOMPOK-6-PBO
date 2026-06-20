package com.rental.infrastructure.persistence;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.rental.domain.entity.Pelanggan;
import com.rental.domain.repository.PelangganRepository;

import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.file.*;
import java.util.*;

public class JsonPelangganRepository implements PelangganRepository {

    private static final String FILE_PATH = "data/pelanggan.json";
    private final Gson gson = new GsonBuilder().setPrettyPrinting().create();
    private final Type listType = new TypeToken<List<Pelanggan>>() {}.getType();

    @Override
    public void simpan(Pelanggan pelanggan) {
        List<Pelanggan> list = readAll();
        list.removeIf(p -> p.getNomorKtp().equals(pelanggan.getNomorKtp()));
        list.add(pelanggan);
        writeAll(list);
    }

    @Override
    public Optional<Pelanggan> cariByKtp(String nomorKtp) {
        return readAll().stream()
                .filter(p -> p.getNomorKtp().equals(nomorKtp))
                .findFirst();
    }

    @Override
    public List<Pelanggan> findAll() {
        return readAll();
    }

    @Override
    public boolean existsByKtp(String nomorKtp) {
        return readAll().stream()
                .anyMatch(p -> p.getNomorKtp().equals(nomorKtp));
    }

    private List<Pelanggan> readAll() {
        try {
            Path path = Paths.get(FILE_PATH);
            if (!Files.exists(path)) return new ArrayList<>();
            String json = Files.readString(path);
            if (json.isBlank()) return new ArrayList<>();
            List<Pelanggan> list = gson.fromJson(json, listType);
            return list != null ? list : new ArrayList<>();
        } catch (IOException e) {
            System.err.println("[ERROR] Gagal membaca " + FILE_PATH + ": " + e.getMessage());
            return new ArrayList<>();
        }
    }

    private void writeAll(List<Pelanggan> list) {
        try {
            Path path = Paths.get(FILE_PATH);
            Files.createDirectories(path.getParent());
            Files.writeString(path, gson.toJson(list));
        } catch (IOException e) {
            System.err.println("[ERROR] Gagal menulis " + FILE_PATH + ": " + e.getMessage());
        }
    }
}
