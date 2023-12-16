package com.example.myapplication1.fragments;

public class item_qadha {

    String prayerId;
    String judul;
    String tanggal;
    String lingkaran;

    public  item_qadha(String prayerId, String judul, String tanggal){
        this.judul = judul;
        this.tanggal= tanggal;
        this.lingkaran=lingkaran;
    }

    public String getPrayerId() {
        return prayerId;
    }

    public void setPrayerId(String prayerId) {
        this.prayerId = prayerId;
    }

    public String getJudul() {
        return judul;
    }

    public void setJudul(String judul) {
        this.judul = judul;
    }

    public String getTanggal() {
        return tanggal;
    }

    public void setTanggal(String tanggal) {
        this.tanggal = tanggal;
    }

    public String getLingkaran() {
        return lingkaran;
    }

    public void setLingkaran(String lingkaran) {
        this.lingkaran = lingkaran;
    }
}
