package com.rental.domain.entity;

public class Pelanggan {
    private String nomorKtp;
    private String namaLengkap;
    private String noTelepon;

    public Pelanggan() {}

    public Pelanggan(String nomorKtp, String namaLengkap, String noTelepon) {
        this.nomorKtp   = nomorKtp.trim();
        this.namaLengkap = namaLengkap.trim();
        this.noTelepon  = noTelepon.trim();
    }
    public String getNomorKtp(){ 
        return nomorKtp; 
    }
    public void setNomorKtp(String k){ 
        this.nomorKtp = k; 
    }
    public String getNamaLengkap(){
        return namaLengkap; 
    }
    public void setNamaLengkap(String n){ 
        this.namaLengkap = n; 
    }
    public String getNoTelepon(){ 
        return noTelepon; 
    }
    public void setNoTelepon(String t){ 
        this.noTelepon = t; 
    }
    @Override
    public String toString(){
        return namaLengkap + " (KTP: " + nomorKtp + ")";
    }
}
