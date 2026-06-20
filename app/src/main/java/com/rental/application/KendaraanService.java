package com.rental.application;

import com.rental.domain.entity.Kendaraan;
import com.rental.domain.entity.Mobil;
import com.rental.domain.entity.Motor;
import com.rental.domain.repository.KendaraanRepository;

import java.util.List;
import java.util.Optional;


public class KendaraanService{

    private final KendaraanRepository kendaraanRepository;

    public KendaraanService(KendaraanRepository kendaraanRepository){
        this.kendaraanRepository = kendaraanRepository;
    }
    public void tambahMobil(String platNomor, String merk, double hargaPerHari,
                            Kendaraan.Kategori kategori, int jumlahPintu){
        validatePlatNomorUnik(platNomor);
        Mobil mobil = new Mobil(platNomor, merk, hargaPerHari, kategori, jumlahPintu);
        kendaraanRepository.simpan(mobil);
    }
    public void tambahMotor(String platNomor, String merk, double hargaPerHari,
                            Kendaraan.Kategori kategori, Motor.Transmisi transmisi) {
        validatePlatNomorUnik(platNomor);
        Motor motor = new Motor(platNomor, merk, hargaPerHari, kategori, transmisi);
        kendaraanRepository.simpan(motor);
    }
    public List<Kendaraan> lihatSemuaKendaraan(){
        return kendaraanRepository.findAll();
    }
    public List<Kendaraan> lihatKendaraanTersedia(){
        return kendaraanRepository.findAllTersedia();
    }
    public void hapusKendaraan(String platNomor){
        Kendaraan kendaraan = kendaraanRepository.cariByPlatNomor(platNomor)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Kendaraan dengan plat '" + platNomor + "' tidak ditemukan."));

        if (!kendaraan.isTersedia()) {
            throw new IllegalStateException(
                    "Kendaraan masih berstatus SEDANG DISEWA, data tidak dapat dihapus!");
        }

        kendaraanRepository.hapus(platNomor);
    }
    public Optional<Kendaraan> cariByPlatNomor(String platNomor){
        return kendaraanRepository.cariByPlatNomor(platNomor);
    }
    public void updateStatus(String platNomor, Kendaraan.Status status){
        Kendaraan kendaraan = kendaraanRepository.cariByPlatNomor(platNomor)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Kendaraan dengan plat '" + platNomor + "' tidak ditemukan."));
        kendaraan.setStatus(status);
        kendaraanRepository.simpan(kendaraan);
    }
    private void validatePlatNomorUnik(String platNomor){
        if (kendaraanRepository.existsByPlatNomor(platNomor)) {
            throw new IllegalArgumentException(
                    "Plat Nomor '" + platNomor + "' sudah terdaftar di sistem.");
        }
    }
}
