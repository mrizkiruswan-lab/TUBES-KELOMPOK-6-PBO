package com.rental.presentation.menu;

import com.rentalapp.application.service.KendaraanService;
import com.rentalapp.application.service.PelangganService;
import com.rentalapp.application.service.TransaksiService;
import com.rentalapp.domain.entity.Kendaraan;
import com.rentalapp.domain.entity.Pelanggan;
import com.rentalapp.domain.entity.Transaksi;
import com.rentalapp.domain.entity.User;
import com.rentalapp.presentation.util.ConsoleHelper;

import java.util.List;
import java.util.Optional;
public class StaffMenu {

    private final PelangganService pelangganService;
    private final KendaraanService kendaraanService;
    private final TransaksiService transaksiService;

    public StaffMenu(PelangganService pelangganService,
    KendaraanService kendaraanService,
    TransaksiService transaksiService) {
        this.pelangganService = pelangganService;
        this.kendaraanService = kendaraanService;
        this.transaksiService = transaksiService;
    }

    public void show(User user) {
        boolean running = true;
        while (running) {
            printDashboard(user.getUsername());
            int pilihan = ConsoleHelper.readInt("Pilihan Anda > ");
            switch (pilihan) {
                case 1  -> daftarPelanggan();
                case 2  -> cariPelanggan();
                case 3  -> cekKendaraanTersedia();
                case 4  -> prosesPeminjaman();
                case 5  -> prosesPengembalian();
                case 0  -> running = false;
                default -> ConsoleHelper.printError("Pilihan tidak valid.");
            }
        }
    }


    private void daftarPelanggan() {
        ConsoleHelper.printHeader("MENU PENDAFTARAN PELANGGAN");
        System.out.println("(ketik 0 pada Nomor KTP untuk kembali)");

        String ktp = ConsoleHelper.readLine("Masukkan Nomor KTP    : ");
        if (ktp.equals("0")) return;

        String nama  = ConsoleHelper.readNonBlank("Masukkan Nama Lengkap : ");
        String telp  = ConsoleHelper.readLine("Masukkan No Telepon   : ");

        try {
            pelangganService.daftarPelanggan(ktp, nama, telp);
            ConsoleHelper.printSuccess("Pelanggan " + nama + " (KTP: " + ktp + ") berhasil didaftarkan.");
        } catch (IllegalArgumentException e) {
            ConsoleHelper.printError(e.getMessage());
        }

        ConsoleHelper.pressEnterToContinue();
    }

    private void cariPelanggan() {
        ConsoleHelper.printHeader("MENU PENCARIAN PELANGGAN");
        System.out.println("(ketik 0 untuk kembali)");

        String ktp = ConsoleHelper.readLine("Masukkan Nomor KTP : ");
        if (ktp.equals("0")) return;

        Optional<Pelanggan> opt = pelangganService.cariByKtp(ktp);
        if (opt.isPresent()) {
            Pelanggan p = opt.get();
            System.out.println("\n[DATA DITEMUKAN]");
            System.out.println("  Nama Lengkap  : " + p.getNamaLengkap());
            System.out.println("  Nomor KTP     : " + p.getNomorKtp());
            System.out.println("  No Telepon    : " + p.getNoTelepon());
        } else {
            ConsoleHelper.printError("Data pelanggan tidak ditemukan.");
        }

        ConsoleHelper.pressEnterToContinue();
    }

    private void cekKendaraanTersedia() {
        ConsoleHelper.printHeader("DAFTAR KENDARAAN TERSEDIA");
        List<Kendaraan> list = kendaraanService.lihatKendaraanTersedia();

        if (list.isEmpty()) {
            System.out.println("  Tidak ada kendaraan yang tersedia saat ini.");
        } else {
            AdminMenu.printKendaraanTable(list);
            System.out.println("  *Catatan: Kendaraan yang sedang disewa tidak ditampilkan.");
        }

        ConsoleHelper.pressEnterToContinue();
    }


    private void prosesPeminjaman() {
        ConsoleHelper.printHeader("MENU PEMINJAMAN KENDARAAN");
        System.out.println("(ketik 0 pada KTP untuk kembali)");

        String ktp = ConsoleHelper.readLine("Masukkan Nomor KTP Pelanggan  : ");
        if (ktp.equals("0")) return;

        String plat    = ConsoleHelper.readNonBlank("Masukkan Plat Nomor Kendaraan : ");
        int    durasi  = ConsoleHelper.readInt("Rencana Durasi Sewa (Hari)    : ");

        System.out.println("\nMemproses transaksi...");

        try {
            Transaksi t = transaksiService.prosesPeminjaman(ktp, plat, durasi);
            printStrukPeminjaman(t);
            ConsoleHelper.printSuccess("Transaksi berhasil dicatat. Status kendaraan berubah menjadi SEDANG DISEWA.");
        } catch (IllegalArgumentException | IllegalStateException e) {
            ConsoleHelper.printError(e.getMessage());
        }

        ConsoleHelper.pressEnterToContinue();
    }

    private void prosesPengembalian() {
        ConsoleHelper.printHeader("MENU PENGEMBALIAN KENDARAAN");
        System.out.println("(ketik 0 untuk kembali)");

        String idTransaksi = ConsoleHelper.readLine("Masukkan ID Transaksi : ");
        if (idTransaksi.equals("0")) return;

        try {
            // Peek at the transaction to show the vehicle before processing
            Transaksi preview = transaksiService.lihatSemuaTransaksi().stream()
                    .filter(t -> t.getIdTransaksi().equalsIgnoreCase(idTransaksi))
                    .findFirst()
                    .orElseThrow(() -> new IllegalArgumentException(
                            "Transaksi dengan ID '" + idTransaksi + "' tidak ditemukan."));

            System.out.println("Kendaraan ditemukan: " + preview.getJenis() +
                    " (" + preview.getPlatNomor() + ").");

            int hariTerlambat = ConsoleHelper.readInt(
                    "Durasi Keterlambatan (Hari, isi 0 jika tepat waktu) : ");

            System.out.println("\nMenghitung tagihan...");
            Transaksi selesai = transaksiService.prosesKembalian(idTransaksi, hariTerlambat);
            printStrukTagihanAkhir(selesai);
            ConsoleHelper.printSuccess("Pengembalian berhasil. Status kendaraan kembali menjadi TERSEDIA.");

        } catch (IllegalArgumentException | IllegalStateException e) {
            ConsoleHelper.printError(e.getMessage());
        }

        ConsoleHelper.pressEnterToContinue();
    }

    private void printStrukPeminjaman(Transaksi t) {
        System.out.println("\n--- STRUK PEMINJAMAN SEMENTARA ---");
        System.out.println("ID Transaksi   : " + t.getIdTransaksi());
        System.out.println("Nama Pelanggan : " + t.getNamaLengkap());
        System.out.println("Kendaraan      : " + t.getJenis() + " (" + t.getPlatNomor() + ")");
        System.out.println("Durasi Sewa    : " + t.getDurasiHari() + " Hari");
        System.out.println("Estimasi Biaya : " + ConsoleHelper.formatRupiah(t.getBiayaDasar()));
        if (t.getUangJaminan() > 0) {
            System.out.println("Uang Jaminan   : " + ConsoleHelper.formatRupiah(t.getUangJaminan()) +
                    "  ← [PREMIUM] akan dikembalikan saat pengembalian");
            System.out.println("TOTAL DIBAYAR  : " + ConsoleHelper.formatRupiah(
                    t.getBiayaDasar() + t.getUangJaminan()));
        }
        System.out.println("----------------------------------");
    }

    private void printStrukTagihanAkhir(Transaksi t) {
        System.out.println("\n--- STRUK TAGIHAN AKHIR ---");
        System.out.println("ID Transaksi   : " + t.getIdTransaksi());
        System.out.println("Pelanggan      : " + t.getNamaLengkap());
        System.out.println("Kendaraan      : " + t.getJenis() + " (" + t.getPlatNomor() + ")");
        System.out.println("Biaya Dasar    : " + ConsoleHelper.formatRupiah(t.getBiayaDasar()) +
                " (" + t.getDurasiHari() + " Hari)");

        if (t.getDendaKeterlambatan() > 0) {
            System.out.println("Denda Telat    : " + ConsoleHelper.formatRupiah(t.getDendaKeterlambatan()));
        }

        if (t.getUangJaminan() > 0) {
            System.out.println("Uang Jaminan   : " + ConsoleHelper.formatRupiah(t.getUangJaminan()) +
                    "  ← [DIKEMBALIKAN ke pelanggan]");
        }

        System.out.println("----------------------------------");
        System.out.println("TOTAL BAYAR    : " + ConsoleHelper.formatRupiah(t.getTotalBayar()));
        System.out.println("----------------------------------");
    }

    private void printDashboard(String username) {
        ConsoleHelper.printHeader("DASHBOARD - STAFF");
        System.out.println("  Selamat Datang, " + username + "!");
        System.out.println("  Silahkan pilih menu:");
        System.out.println("  1. Daftar Pelanggan Baru");
        System.out.println("  2. Cari Data Pelanggan");
        System.out.println("  3. Cek Kendaraan Tersedia");
        System.out.println("  4. Proses Peminjaman (Sewa)");
        System.out.println("  5. Proses Pengembalian");
        System.out.println("  0. Logout");
    }
}
