package com.rental.domain.entity;

public abstract class Kendaraan{

    public enum Status{
        TERSEDIA, SEDANG_DISEWA
    }
    public enum Kategori{
        REGULER, PREMIUM
    }
    private String platNomor;
    private String merk;
    private double hargaPerHari;
    private Status status;
    private Kategori kategori;

    public Kendaraan(){
    }
    public Kendaraan(String platNomor, String merk, double hargaPerHari, Kategori kategori){
        this.platNomor   = platNomor.toUpperCase().trim();
        this.merk        = merk.trim();
        this.hargaPerHari= hargaPerHari;
        this.status      = Status.TERSEDIA;
        this.kategori    = kategori;
    }
    public abstract String getJenis();

     public abstract String getInfoTambahan();

    public abstract double getDendaPerHari();

    public boolean isPremium(){
        return Kategori.PREMIUM.equals(this.kategori);
    }
    public boolean isTersedia(){
        return Status.TERSEDIA.equals(this.status);
    }
    public double hitungBiayaDasar(int durasiHari){
        return hargaPerHari * durasiHari;
    }
    public double hitungDenda(int hariTerlambat) {
        if (hariTerlambat <= 0) return 0;
        return getDendaPerHari() * hariTerlambat;
    }
    public String getPlatNomor(){ 
        return platNomor;
    }
    public void setPlatNomor(String p){ 
        this.platNomor = p.toUpperCase().trim();
    }
    public String getMerk(){ 
        return merk;
    }
    public void setMerk(String m){ 
        this.merk = m;
    }
    public double getHargaPerHari(){ 
        return hargaPerHari; 
    }
    public void setHargaPerHari(double h){ 
        this.hargaPerHari = h;
    }
    public Status getStatus(){
         return status;
    }
    public void setStatus(Status s){
         this.status = s;
    }
    public Kategori getKategori(){ 
        return kategori; 
    }
    public void setKategori(Kategori k){ 
        this.kategori = k; 
    }
}
