package com.esprit.bikeit.Model;

/**
 * Created by dell on 15/01/2018.
 */

public class User {
    private int idUser;
    private String name;
    private String email;
    private  String password;
    private String avatar;
    public String score;
    public double freeHours;
    private int rank;


    public User() {
    }


    public User(int idUser) {
        this.idUser = idUser;
    }
    public User(int idUser, String name, String email, String password) {
        this.idUser = idUser;
        this.name = name;
        this.email = email;
        this.password = password;
    }

    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    public int getIdUser() {
        return idUser;
    }

    public void setIdUser(int idUser) {
        this.idUser = idUser;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAvatar() {return avatar;}

    public void setAvatar(String avatar) {this.avatar = avatar;}



    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public double getFreeHours() {
        return freeHours;
    }

    public void setFreeHours(double freeHours) {
        this.freeHours = freeHours;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        User user = (User) o;

        if (idUser != user.idUser) return false;
        if (name != null ? !name.equals(user.name) : user.name != null)
            return false;
        if (email != null ? !email.equals(user.email) : user.email != null) return false;
        return password != null ? password.equals(user.password) : user.password == null;

    }

    @Override
    public int hashCode() {
        int result = idUser;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (email != null ? email.hashCode() : 0);
        result = 31 * result + (password != null ? password.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "User{" +
                "idUser=" + idUser +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", avatar='" + avatar + '\'' +
                ", score='" + score + '\'' +
                ", freeHours=" + freeHours +
                '}';
    }
}
