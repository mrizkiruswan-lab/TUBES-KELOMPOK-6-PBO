package com.rental.presentation.menu;

import com.rental.application.service.TransaksiService;
import com.rental.domain.entity.Transaksi;
import com.rental.domain.entity.User;
import com.rental.presentation.util.ConsoleHelper;

import java.util.List;

public class OwnerMenu {

    private final TransaksiService transaksiService;

    public OwnerMenu(TransaksiService transaksiService) {
        this.transaksiService = transaksiService;
    }

    public void show(User user) {
        boolean running = true;
        while (running) {
            printDashboard(user.getUsername());
            int pilihan = ConsoleHelper.readInt("Pilihan Anda > ");
            switch (pilihan) {
                case 1  -> lihatLaporan();
                case 0  -> running = false;
                default -> ConsoleHelper.printError("Pilihan tidak valid.");
            }
        }
    }

    private void lihatLaporan() {
        ConsoleHelper.printHeader("LAPORAN RIWAYAT & PENDAPATAN");

        List<Transaksi> list = transaksiService.lihatSemuaTransaksi();

        if (list.isEmpty()) {
            System.out.println("  Belum ada riwayat transaksi.");
        } else {
            String fmt    = "| %-10s | %-20s | %-14s | %-10s | %-14s |\n";
            String border = "-".repeat(80);
            System.out.println(border);
            System.out.printf(fmt, "ID Transaksi", "Pelanggan", "Kendaraan", "Status", "Total Tagihan");
            System.out.println(border);
            for (Transaksi t : list) {
                String total = Transaksi.StatusTransaksi.SELESAI.equals(t.getStatus())
                        ? ConsoleHelper.formatRupiah(t.getTotalBayar())
                        : "-";
                System.out.printf(fmt,
                        t.getIdTransaksi(),
                        t.getNamaLengkap(),
                        t.getPlatNomor(),
                        t.getStatus(),
                        total);
            }
            System.out.println(border);
        }

        double total = transaksiService.hitungTotalPendapatan();
        System.out.println("\nTOTAL PENDAPATAN (Hanya dari Transaksi Selesai): "
                + ConsoleHelper.formatRupiah(total));
        System.out.println("=".repeat(80));

        ConsoleHelper.pressEnterToContinue();
    }

    private void printDashboard(String username) {
        ConsoleHelper.printHeader("DASHBOARD - OWNER");
        System.out.println("  Selamat Datang, " + username + ".");
        System.out.println("  Silahkan pilih menu:");
        System.out.println("  1. Lihat Laporan Pendapatan & Riwayat");
        System.out.println("  0. Logout");
    }
}
