package com.rental.domain.repository;

import com.rental.domain.entity.Kendaraan;
import java.util.List;
import java.util.Optional;

public interface KendaraanRepository{
    void simpan(Kendaraan kendaraan);
    void hapus(String platNomor);
    Optional<Kendaraan> cariByPlatNomor(String platNomor);
    List<Kendaraan> findAll();
    List<Kendaraan> findAllTersedia();
    boolean existsByPlatNomor(String platNomor);
}
