package com.esprit.bikeit.Model;

import java.util.Date;

/**
 * Created by dell on 21/01/2018.
 */

public class Defi {
    public int id;
    public String name;
    public String desc;
    public String price;
    private double distance;
    private  boolean subed;
    private String etat;

    public Date created;



    public Defi(String name, String desc, String price, Date created) {
        this.name = name;
        this.desc = desc;
        this.price = price;
        this.created = created;
    }

    public Defi(int id, String name, String desc, String price, Date created) {
        this.id = id;
        this.name = name;
        this.desc = desc;
        this.price = price;
        this.created = created;
    }

    public String getEtat() {
        return etat;
    }

    public void setEtat(String etat) {
        this.etat = etat;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public boolean isSubed() {
        return subed;
    }

    public void setSubed(boolean subed) {
        this.subed = subed;
    }

    @Override
    public String toString() {
        return "Defi{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", desc='" + desc + '\'' +
                ", price='" + price + '\'' +
                ", distance=" + distance +
                ", subed=" + subed +
                ", created=" + created +
                '}';
    }
}
