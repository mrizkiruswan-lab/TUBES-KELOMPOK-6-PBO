package com.rental.domain.repository;

import com.rental.domain.entity.Pelanggan;
import java.util.List;
import java.util.Optional;

public interface PelangganRepository {
    void simpan(Pelanggan pelanggan);
    Optional<Pelanggan> cariByKtp(String nomorKtp);
    List<Pelanggan> findAll();
    boolean existsByKtp(String nomorKtp);
}
