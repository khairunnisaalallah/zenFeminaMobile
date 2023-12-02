package com.example.myapplication1;

public class item {
    String judul;
    String isi;
    String image;

    public item(String image, String judul, String isi) {
        this.judul = judul;
        this.isi = isi;
        this.image = image;
    }

    public String getJudul() {
        return judul;
    }

    public void setJudul(String judul) {
        this.judul = judul;
    }

    public String getIsi() {
        return isi;
    }

    public void setIsi(String isi) {
        this.isi = isi;

    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}