package com.rental.domain.entity;

public class Mobil extends Kendaraan{

    private static final double DENDA_PER_HARI = 50_000;

    private int jumlahPintu;

    public Mobil(){
    }
    public Mobil(String platNomor, String merk, double hargaPerHari,
                 Kategori kategori, int jumlahPintu) {
        super(platNomor, merk, hargaPerHari, kategori);
        this.jumlahPintu = jumlahPintu;
    }
    @Override
    public String getJenis(){
        return "Mobil";
    }
    @Override
    public String getInfoTambahan(){
        return jumlahPintu + " pintu";
    }
    @Override
    public double getDendaPerHari(){
        return DENDA_PER_HARI;
    }
    public int getJumlahPintu(){ 
        return jumlahPintu;
    }
    public void setJumlahPintu(int j){ 
        this.jumlahPintu = j; 
    }
}
