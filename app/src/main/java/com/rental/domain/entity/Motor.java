package com.rental.domain.entity;

public class Motor extends Kendaraan{

    private static final double DENDA_PER_HARI = 20_000;

    public enum Transmisi{ MANUAL, MATIC }

    private Transmisi transmisi;

    public Motor(){
    }
    public Motor(String platNomor, String merk, double hargaPerHari,Kategori kategori, Transmisi transmisi){
        super(platNomor, merk, hargaPerHari, kategori);
        this.transmisi = transmisi;
    }
    @Override
    public String getJenis(){
        return "Motor";
    }
    @Override
    public String getInfoTambahan(){
        return transmisi != null ? transmisi.name() : "-";
    }
    @Override
    public double getDendaPerHari(){
        return DENDA_PER_HARI;
    }
    public Transmisi getTransmisi(){ 
        return transmisi; 
    }
    public void setTransmisi(Transmisi t){
         this.transmisi = t; 
    }
}
