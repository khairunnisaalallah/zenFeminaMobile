package com.example.myapplication1;

public class item {
    String judul;
    String isi;
    int image;

    public item(String judul,String isi, int image){
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

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }
}

