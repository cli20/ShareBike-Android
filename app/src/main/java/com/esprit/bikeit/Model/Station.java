package com.esprit.bikeit.Model;

/**
 * Created by dell on 04/02/2018.
 */

public class Station {

    private int id;
    private String nom;
    private double longitude;
    private double Latitude;
    private int nbVelo;
    private int nbtt;

    public Station() {
    }

    public Station(String nom, int nbVelo) {
        this.nom = nom;
        this.nbVelo = nbVelo;
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

    public int getNbVelo() {
        return nbVelo;
    }

    public void setNbVelo(int nbVelo) {
        this.nbVelo = nbVelo;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return Latitude;
    }

    public void setLatitude(double         latitude) {
        Latitude = latitude;
    }

    public int getNbtt() {
        return nbtt;
    }

    public void setNbtt(int nbtt) {
        this.nbtt = nbtt;
    }

    @Override
    public String toString() {
        return "Station{" +
                "id=" + id +
                ", nom='" + nom + '\'' +
                ", longitude=" + longitude +
                ", Latitude=" + Latitude +
                ", nbVelo=" + nbVelo +
                '}';
    }
}
