package com.esprit.bikeit.Model;

import java.util.Date;

public class Post {
    private   String img;
    private String nom;
    private Date date;

    public Post(String img, String nom, Date date) {
        this.img = img;
        this.nom = nom;
        this.date = date;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "Post{" +
                "img='" + img + '\'' +
                ", nom='" + nom + '\'' +
                ", date=" + date +
                '}';
    }
}
