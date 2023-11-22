package com.example.myapplication1;

public class item {
String judul;
int image;

public item(String judul, int image){
    this.judul = judul;
    this.image = image;
}

    public String getJudul() {
        return judul;
    }

    public void setJudul(String judul) {
        this.judul = judul;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }
}

