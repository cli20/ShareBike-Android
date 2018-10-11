package com.esprit.bikeit.Model;

/**
 * Created by dell on 18/02/2018.
 */

public class Velo {

    private int id;
    private String nom;
    private String marque;
    private String etat;
    private  String image;
    private int id_boitier;
    private  int id_proprietaire;
    private double lat;
    private  double lng;

    public Velo(int id, String nom, String marque, String etat, String image, int id_boitier, int id_proprietaire) {
        this.id = id;
        this.nom = nom;
        this.marque = marque;
        this.etat = etat;
        this.image = image;
        this.id_boitier = id_boitier;
        this.id_proprietaire = id_proprietaire;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getMarque() {
        return marque;
    }

    public void setMarque(String marque) {
        this.marque = marque;
    }

    public String getEtat() {
        return etat;
    }

    public void setEtat(String etat) {
        this.etat = etat;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public int getId_boitier() {
        return id_boitier;
    }

    public void setId_boitier(int id_boitier) {
        this.id_boitier = id_boitier;
    }

    public int getId_proprietaire() {
        return id_proprietaire;
    }

    public void setId_proprietaire(int id_proprietaire) {
        this.id_proprietaire = id_proprietaire;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    @Override
    public String toString() {
        return "Velo{" +
                "id=" + id +
                ", nom='" + nom + '\'' +
                ", marque='" + marque + '\'' +
                ", etat='" + etat + '\'' +
                ", image='" + image + '\'' +
                ", id_boitier=" + id_boitier +
                ", id_proprietaire=" + id_proprietaire +
                '}';
    }
}
