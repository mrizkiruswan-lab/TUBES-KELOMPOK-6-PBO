package com.rental.application;

import com.rental.domain.entity.Pelanggan;
import com.rental.domain.repository.PelangganRepository;

import java.util.List;
import java.util.Optional;

public class PelangganService {

    private final PelangganRepository pelangganRepository;

    public PelangganService(PelangganRepository pelangganRepository) {
        this.pelangganRepository = pelangganRepository;
    }

    public void daftarPelanggan(String nomorKtp, String namaLengkap, String noTelepon) {
        if (nomorKtp == null || nomorKtp.isBlank()) {
            throw new IllegalArgumentException("Nomor KTP tidak boleh kosong.");
        }
        if (!nomorKtp.matches("\\d+")) {
            throw new IllegalArgumentException("Nomor KTP hanya boleh berisi angka.");
        }
        if (pelangganRepository.existsByKtp(nomorKtp)) {
            throw new IllegalArgumentException(
                    "Pelanggan dengan KTP '" + nomorKtp + "' sudah terdaftar.");
        }
        if (namaLengkap == null || namaLengkap.isBlank()) {
            throw new IllegalArgumentException("Nama Lengkap tidak boleh kosong.");
        }

        Pelanggan p = new Pelanggan(nomorKtp, namaLengkap, noTelepon);
        pelangganRepository.simpan(p);
    }

    public Optional<Pelanggan> cariByKtp(String nomorKtp) {
        return pelangganRepository.cariByKtp(nomorKtp);
    }

    public List<Pelanggan> lihatSemuaPelanggan() {
        return pelangganRepository.findAll();
    }
}
