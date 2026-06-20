package com.rental.infrastructure.persistence;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.rental.domain.entity.Transaksi;
import com.rental.domain.repository.TransaksiRepository;

import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.file.*;
import java.util.*;
import java.util.stream.Collectors;

public class JsonTransaksiRepository implements TransaksiRepository {

    private static final String FILE_PATH = "data/transaksi.json";
    private final Gson gson = new GsonBuilder().setPrettyPrinting().create();
    private final Type listType = new TypeToken<List<Transaksi>>() {}.getType();

    @Override
    public void simpan(Transaksi transaksi) {
        List<Transaksi> list = readAll();
        list.add(transaksi);
        writeAll(list);
    }

    @Override
    public void update(Transaksi transaksi) {
        List<Transaksi> list = readAll();
        list.replaceAll(t -> t.getIdTransaksi().equals(transaksi.getIdTransaksi()) ? transaksi : t);
        writeAll(list);
    }

    @Override
    public Optional<Transaksi> cariById(String idTransaksi) {
        return readAll().stream()
                .filter(t -> t.getIdTransaksi().equals(idTransaksi))
                .findFirst();
    }

    @Override
    public Optional<Transaksi> cariAktifByPlatNomor(String platNomor) {
        return readAll().stream()
                .filter(t -> t.getPlatNomor().equalsIgnoreCase(platNomor)
                          && Transaksi.StatusTransaksi.BERJALAN.equals(t.getStatus()))
                .findFirst();
    }

    @Override
    public List<Transaksi> findAll() {
        return readAll();
    }

    @Override
    public String generateNextId() {
        List<Transaksi> list = readAll();
        int max = list.stream()
                .map(Transaksi::getIdTransaksi)
                .filter(id -> id != null && id.startsWith("TRX-"))
                .mapToInt(id -> {
                    try { return Integer.parseInt(id.substring(4)); }
                    catch (NumberFormatException e) { return 0; }
                })
                .max()
                .orElse(0);
        return String.format("TRX-%03d", max + 1);
    }

    private List<Transaksi> readAll() {
        try {
            Path path = Paths.get(FILE_PATH);
            if (!Files.exists(path)) return new ArrayList<>();
            String json = Files.readString(path);
            if (json.isBlank()) return new ArrayList<>();
            List<Transaksi> list = gson.fromJson(json, listType);
            return list != null ? list : new ArrayList<>();
        } catch (IOException e) {
            System.err.println("[ERROR] Gagal membaca " + FILE_PATH + ": " + e.getMessage());
            return new ArrayList<>();
        }
    }

    private void writeAll(List<Transaksi> list) {
        try {
            Path path = Paths.get(FILE_PATH);
            Files.createDirectories(path.getParent());
            Files.writeString(path, gson.toJson(list));
        } catch (IOException e) {
            System.err.println("[ERROR] Gagal menulis " + FILE_PATH + ": " + e.getMessage());
        }
    }
}
