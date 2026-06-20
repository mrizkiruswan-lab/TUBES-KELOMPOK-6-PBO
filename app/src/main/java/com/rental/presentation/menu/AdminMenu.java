package com.rental.presentation.menu;

import com.rental.application.KendaraanService;
import com.rental.domain.entity.Kendaraan;
import com.rental.domain.entity.Motor;
import com.rental.domain.entity.User;
import com.rental.presentation.util.ConsoleHelper;

import java.util.List;

public class AdminMenu {

    private final KendaraanService kendaraanService;

    public AdminMenu(KendaraanService kendaraanService) {
        this.kendaraanService = kendaraanService;
    }

    public void show(User user) {
        boolean running = true;
        while (running) {
            printDashboard(user.getUsername());
            int pilihan = ConsoleHelper.readInt("Pilihan Anda > ");
            switch (pilihan) {
                case 1  -> tambahKendaraan();
                case 2  -> lihatSemuaKendaraan();
                case 3  -> hapusKendaraan();
                case 0  -> running = false;
                default -> ConsoleHelper.printError("Pilihan tidak valid.");
            }
        }
    }


    private void tambahKendaraan() {
        ConsoleHelper.printHeader("MENU TAMBAH KENDARAAN BARU");
        System.out.println("Pilih Jenis Kendaraan:");
        System.out.println("  1. Mobil");
        System.out.println("  2. Motor");
        System.out.println("  0. Kembali");
        int jenis = ConsoleHelper.readInt("Pilihan Anda > ");

        if (jenis == 0) return;
        if (jenis != 1 && jenis != 2) {
            ConsoleHelper.printError("Pilihan jenis tidak valid.");
            return;
        }

        String platNomor = ConsoleHelper.readNonBlank("Masukkan Plat Nomor       : ");
        double harga     = ConsoleHelper.readDouble("Masukkan Harga Sewa/Hari  : Rp ");
        String merk      = ConsoleHelper.readNonBlank("Masukkan Merk Kendaraan   : ");

        Kendaraan.Kategori kategori = readKategori();

        try {
            if (jenis == 1) {
                int pintu = ConsoleHelper.readInt("Masukkan Jumlah Pintu     : ");
                kendaraanService.tambahMobil(platNomor, merk, harga, kategori, pintu);
            } else {
                Motor.Transmisi transmisi = readTransmisi();
                kendaraanService.tambahMotor(platNomor, merk, harga, kategori, transmisi);
            }
            String jenisStr = jenis == 1 ? "Mobil" : "Motor";
            ConsoleHelper.printSuccess(jenisStr + " dengan plat " + platNomor.toUpperCase() +
                    " berhasil ditambahkan dengan status TERSEDIA." +
                    (kategori == Kendaraan.Kategori.PREMIUM ? " [PREMIUM]" : ""));
        } catch (IllegalArgumentException e) {
            ConsoleHelper.printError(e.getMessage());
        }

        ConsoleHelper.pressEnterToContinue();
    }

    private void lihatSemuaKendaraan() {
        ConsoleHelper.printHeader("DAFTAR SELURUH KENDARAAN");
        List<Kendaraan> list = kendaraanService.lihatSemuaKendaraan();

        if (list.isEmpty()) {
            System.out.println("  Data kendaraan masih kosong.");
        } else {
            printKendaraanTable(list);
        }

        ConsoleHelper.pressEnterToContinue();
    }


    private void hapusKendaraan() {
        ConsoleHelper.printHeader("MENU HAPUS KENDARAAN");
        System.out.println("(ketik 0 untuk kembali)");

        while (true) {
            String plat = ConsoleHelper.readLine("Masukkan Plat Nomor yang ingin dihapus : ");
            if (plat.equals("0")) return;

            try {
                kendaraanService.hapusKendaraan(plat);
                ConsoleHelper.printSuccess("Kendaraan " + plat.toUpperCase() + " berhasil dihapus dari sistem.");
                ConsoleHelper.pressEnterToContinue();
                return;
            } catch (IllegalArgumentException | IllegalStateException e) {
                ConsoleHelper.printError(e.getMessage());
            }
        }
    }

    private Kendaraan.Kategori readKategori() {
        while (true) {
            System.out.println("Pilih Kategori Kendaraan:");
            System.out.println("  1. REGULER");
            System.out.println("  2. PREMIUM (Uang Jaminan Rp 500.000 akan dikenakan saat sewa)");
            int k = ConsoleHelper.readInt("Pilihan Anda > ");
            if (k == 1) return Kendaraan.Kategori.REGULER;
            if (k == 2) return Kendaraan.Kategori.PREMIUM;
            ConsoleHelper.printError("Pilihan tidak valid.");
        }
    }

    private Motor.Transmisi readTransmisi() {
        while (true) {
            System.out.println("Pilih Jenis Transmisi: 1. Manual   2. Matic");
            int t = ConsoleHelper.readInt("Pilihan Anda > ");
            if (t == 1) return Motor.Transmisi.MANUAL;
            if (t == 2) return Motor.Transmisi.MATIC;
            ConsoleHelper.printError("Pilihan tidak valid.");
        }
    }

    static void printKendaraanTable(List<Kendaraan> list) {
        String fmt = "| %-14s | %-5s | %-10s | %-12s | %-18s | %-10s | %-7s |\n";
        String border = "-".repeat(100);
        System.out.println(border);
        System.out.printf(fmt, "Plat Nomor", "Jenis", "Harga/Hari",
                "Merk", "Info Tambahan", "Status", "Kategori");
        System.out.println(border);
        for (Kendaraan k : list) {
            System.out.printf(fmt,
                    k.getPlatNomor(),
                    k.getJenis(),
                    ConsoleHelper.formatRupiah(k.getHargaPerHari()),
                    k.getMerk(),
                    k.getInfoTambahan(),
                    k.getStatus(),
                    k.getKategori());
        }
        System.out.println(border);
    }

    private void printDashboard(String username) {
        ConsoleHelper.printHeader("DASHBOARD - ADMIN");
        System.out.println("  Selamat Datang, " + username + "!");
        System.out.println("  Silahkan pilih menu:");
        System.out.println("  1. Tambah Kendaraan Baru");
        System.out.println("  2. Lihat Semua Kendaraan");
        System.out.println("  3. Hapus Kendaraan");
        System.out.println("  0. Logout");
    }
}
