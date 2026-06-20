package com.rental.application.service;

import com.rental.domain.entity.Kendaraan;
import com.rental.domain.entity.Pelanggan;
import com.rental.domain.entity.Transaksi;
import com.rental.domain.repository.KendaraanRepository;
import com.rental.domain.repository.PelangganRepository;
import com.rental.domain.repository.TransaksiRepository;

import java.util.List;

public class TransaksiService {

    private final TransaksiRepository  transaksiRepository;
    private final KendaraanRepository  kendaraanRepository;
    private final PelangganRepository  pelangganRepository;

    public TransaksiService(TransaksiRepository transaksiRepository,
                            KendaraanRepository kendaraanRepository,
                            PelangganRepository pelangganRepository) {
        this.transaksiRepository = transaksiRepository;
        this.kendaraanRepository = kendaraanRepository;
        this.pelangganRepository = pelangganRepository;
    }

    
    public Transaksi prosesPeminjaman(String nomorKtp, String platNomor, int durasiHari) {
        if (durasiHari <= 0) {
            throw new IllegalArgumentException("Durasi sewa harus lebih dari 0 hari.");
        }

        Pelanggan pelanggan = pelangganRepository.cariByKtp(nomorKtp)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Pelanggan dengan KTP '" + nomorKtp + "' tidak ditemukan. " +
                        "Silakan daftarkan pelanggan terlebih dahulu."));

        Kendaraan kendaraan = kendaraanRepository.cariByPlatNomor(platNomor)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Kendaraan dengan plat '" + platNomor + "' tidak ditemukan."));

        if (!kendaraan.isTersedia()) {
            throw new IllegalStateException(
                    "Kendaraan '" + platNomor + "' sedang disewa dan tidak tersedia.");
        }

        String idTransaksi = transaksiRepository.generateNextId();

        Transaksi transaksi = new Transaksi(idTransaksi, pelanggan, kendaraan, durasiHari);

        kendaraan.setStatus(Kendaraan.Status.SEDANG_DISEWA);
        kendaraanRepository.simpan(kendaraan);

        transaksiRepository.simpan(transaksi);

        return transaksi;
    }

    public Transaksi prosesKembalian(String idTransaksi, int hariTerlambat) {
        if (hariTerlambat < 0) {
            throw new IllegalArgumentException("Hari keterlambatan tidak boleh negatif.");
        }

        Transaksi transaksi = transaksiRepository.cariById(idTransaksi)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Transaksi dengan ID '" + idTransaksi + "' tidak ditemukan."));

        if (Transaksi.StatusTransaksi.SELESAI.equals(transaksi.getStatus())) {
            throw new IllegalStateException(
                    "Transaksi '" + idTransaksi + "' sudah selesai sebelumnya.");
        }

        Kendaraan kendaraan = kendaraanRepository.cariByPlatNomor(transaksi.getPlatNomor())
                .orElseThrow(() -> new IllegalStateException(
                        "Data kendaraan tidak konsisten. Hubungi administrator."));

        double denda = kendaraan.hitungDenda(hariTerlambat);

        transaksi.selesaikan(denda);
        transaksiRepository.update(transaksi);

        kendaraan.setStatus(Kendaraan.Status.TERSEDIA);
        kendaraanRepository.simpan(kendaraan);

        return transaksi;
    }


    public List<Transaksi> lihatSemuaTransaksi() {
        return transaksiRepository.findAll();
    }

    public double hitungTotalPendapatan() {
        return transaksiRepository.findAll().stream()
                .filter(t -> Transaksi.StatusTransaksi.SELESAI.equals(t.getStatus()))
                .mapToDouble(Transaksi::getTotalBayar)
                .sum();
    }

    public Transaksi cariTransaksiAktifByPlat(String platNomor) {
        return transaksiRepository.cariAktifByPlatNomor(platNomor)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Tidak ada transaksi aktif untuk kendaraan '" + platNomor + "'."));
    }
}
