package com.rental.infrastructure.persistence;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.rental.domain.entity.Kendaraan;
import com.rental.domain.repository.KendaraanRepository;
import com.rental.infrastructure.util.GsonFactory;

import java.io.*;
import java.lang.reflect.Type;
import java.nio.file.*;
import java.util.*;
import java.util.stream.Collectors;

public class JsonKendaraanRepository implements KendaraanRepository {

    private static final String FILE_PATH = "data/kendaraan.json";
    private final Gson gson = GsonFactory.getInstance();
    private final Type listType = new TypeToken<List<Kendaraan>>() {}.getType();

    @Override
    public void simpan(Kendaraan kendaraan){
        List<Kendaraan> list = readAll();
        list.removeIf(k -> k.getPlatNomor().equalsIgnoreCase(kendaraan.getPlatNomor()));
        list.add(kendaraan);
        writeAll(list);
    }
    @Override
    public void hapus(String platNomor){
        List<Kendaraan> list = readAll();
        list.removeIf(k -> k.getPlatNomor().equalsIgnoreCase(platNomor));
        writeAll(list);
    }
    @Override
    public Optional<Kendaraan> cariByPlatNomor(String platNomor){
        return readAll().stream()
                .filter(k -> k.getPlatNomor().equalsIgnoreCase(platNomor))
                .findFirst();
    }
    @Override
    public List<Kendaraan> findAll(){
        return readAll();
    }
    @Override
    public List<Kendaraan> findAllTersedia(){
        return readAll().stream()
                .filter(Kendaraan::isTersedia)
                .collect(Collectors.toList());
    }
    @Override
    public boolean existsByPlatNomor(String platNomor){
        return readAll().stream()
                .anyMatch(k -> k.getPlatNomor().equalsIgnoreCase(platNomor));
    }
    private List<Kendaraan> readAll(){
        try {
            Path path = Paths.get(FILE_PATH);
            if (!Files.exists(path)) return new ArrayList<>();
            String json = Files.readString(path);
            if (json.isBlank()) return new ArrayList<>();
            List<Kendaraan> list = gson.fromJson(json, listType);
            return list != null ? list : new ArrayList<>();
        } catch (IOException e) {
            System.err.println("[ERROR] Gagal membaca " + FILE_PATH + ": " + e.getMessage());
            return new ArrayList<>();
        }
    }
    private void writeAll(List<Kendaraan> list){
        try {
            Path path = Paths.get(FILE_PATH);
            Files.createDirectories(path.getParent());
            Files.writeString(path, gson.toJson(list));
        } catch (IOException e) {
            System.err.println("[ERROR] Gagal menulis " + FILE_PATH + ": " + e.getMessage());
        }
    }
}
