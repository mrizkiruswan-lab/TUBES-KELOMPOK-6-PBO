package com.rental.domain.repository;

import com.rental.domain.entity.Transaksi;
import java.util.List;
import java.util.Optional;

public interface TransaksiRepository {
    void simpan(Transaksi transaksi);
    void update(Transaksi transaksi);
    Optional<Transaksi> cariById(String idTransaksi);
    Optional<Transaksi> cariAktifByPlatNomor(String platNomor);
    List<Transaksi>  findAll();
    String generateNextId();
}
