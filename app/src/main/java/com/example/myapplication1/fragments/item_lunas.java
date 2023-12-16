package com.example.myapplication1.fragments;

public class item_lunas {
    String id_prayer;
    String title;
    String date;
    String done;

    public  item_lunas(String id_prayer, String title, String date){
        this.id_prayer= id_prayer;
        this.title  = title;
        this.date= date;
        this.done=done;
    }

    public String getId_prayer() {
        return id_prayer;
    }

    public void setId_prayer(String id_prayer) {
        this.id_prayer = id_prayer;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDone() {
        return done;
    }

    public void setDone(String done) {
        this.done = done;
    }
}
