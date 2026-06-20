package com.rental.domain.entity;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Transaksi {

    public enum StatusTransaksi {
        BERJALAN, SELESAI
    }

    private static final double UANG_JAMINAN_PREMIUM = 500_000;
    private static final DateTimeFormatter FORMATTER =
            DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");

    private String idTransaksi;
    private String nomorKtp;       
    private String platNomor;      
    private String namaLengkap;    
    private String jenis;          
    private int durasiHari;
    private double hargaPerHari;
    private double uangJaminan;    
    private double biayaDasar;
    private double dendaKeterlambatan;
    private double totalBayar;
    private StatusTransaksi status;
    private String tanggalMulai;
    private String tanggalSelesai;

    public Transaksi() {}

    public Transaksi(String idTransaksi, Pelanggan pelanggan, Kendaraan kendaraan, int durasiHari) {
        this.idTransaksi = idTransaksi;
        this.nomorKtp = pelanggan.getNomorKtp();
        this.namaLengkap = pelanggan.getNamaLengkap();
        this.platNomor = kendaraan.getPlatNomor();
        this.jenis = kendaraan.getJenis();
        this.durasiHari = durasiHari;
        this.hargaPerHari = kendaraan.getHargaPerHari();
        this.uangJaminan = kendaraan.isPremium() ? UANG_JAMINAN_PREMIUM : 0;
        this.biayaDasar = kendaraan.hitungBiayaDasar(durasiHari);
        this.dendaKeterlambatan = 0;
        this.totalBayar = biayaDasar + uangJaminan;
        this.status = StatusTransaksi.BERJALAN;
        this.tanggalMulai  = LocalDateTime.now().format(FORMATTER);
        this.tanggalSelesai = null;
    }

    public void selesaikan(double denda) {
        this.dendaKeterlambatan = denda;
        this.biayaDasar = hargaPerHari * durasiHari;
        this.totalBayar = biayaDasar + denda;  
        this.status = StatusTransaksi.SELESAI;
        this.tanggalSelesai = LocalDateTime.now().format(FORMATTER);
    }

    public static double getUangJaminanPremium() {
        return UANG_JAMINAN_PREMIUM;
    }
    public String getIdTransaksi() { 
        return idTransaksi; 
    }
    public void setIdTransaksi(String id) { 
        this.idTransaksi = id; 
    }
    public String getNomorKtp() { 
        return nomorKtp; 
    }
    public void setNomorKtp(String k) { 
        this.nomorKtp = k; 
    }
    public String getPlatNomor() {
        return platNomor; 
    }
    public void setPlatNomor(String p) { 
        this.platNomor = p; 
    }
    public String getNamaLengkap() { 
        return namaLengkap; 
    }
    public void setNamaLengkap(String n) { 
        this.namaLengkap = n; 
    }
    public String getJenis() { 
        return jenis; 
    }
    public void setJenis(String j) { 
        this.jenis = j; 
    }
    public int getDurasiHari() { 
        return durasiHari; 
    }
    public void setDurasiHari(int d) { 
        this.durasiHari = d; 
    }
    public double getHargaPerHari() { 
        return hargaPerHari; 
    }
    public void setHargaPerHari(double h) { 
        this.hargaPerHari = h; 
    }
    public double getUangJaminan() { 
        return uangJaminan; 
    }
    public void setUangJaminan(double u) { 
        this.uangJaminan = u; 
    }
    public double getBiayaDasar() { 
        return biayaDasar; 
    }
    public void setBiayaDasar(double b) { 
        this.biayaDasar = b; 
    }
    public double getDendaKeterlambatan() { 
        return dendaKeterlambatan; 
    }
    public void setDendaKeterlambatan(double d) { 
        this.dendaKeterlambatan = d; 
    }
    public double getTotalBayar() { 
        return totalBayar; 
    }
    public void setTotalBayar(double t) { 
        this.totalBayar = t; 
    }
    public StatusTransaksi  getStatus() { 
        return status;
    }
    public void setStatus(StatusTransaksi s) { 
        this.status = s; 
    }
    public String getTanggalMulai() { 
        return tanggalMulai; 
    }
    public void setTanggalMulai(String t) { 
        this.tanggalMulai = t; 
    }
    public String getTanggalSelesai() { 
        return tanggalSelesai; 
    }
    public void setTanggalSelesai(String t) { 
        this.tanggalSelesai = t; 
    }
}
