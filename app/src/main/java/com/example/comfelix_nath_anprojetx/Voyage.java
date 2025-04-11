package com.example.comfelix_nath_anprojetx;

import java.io.Serializable;
import java.util.List;

public class Voyage implements Serializable {
    private int id, duree_jours;
    private String nom_voyage, description, destination, image_url, type_de_voyage, activites_incluses;
    private double prix;
    private List<Trip> trips;
    public Voyage(int id, int duree_jours, String nom_voyage, String description, String destination, String image_url, String type_de_voyage, String activites_incluses, double prix, List<Trip> trips) {
        this.id = id;
        this.duree_jours = duree_jours;
        this.nom_voyage = nom_voyage;
        this.description = description;
        this.destination = destination;
        this.image_url = image_url;
        this.type_de_voyage = type_de_voyage;
        this.activites_incluses = activites_incluses;
        this.prix = prix;
        this.trips = trips;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getDuree() {
        return duree_jours;
    }

    public void setDuree(int duree_jours) {
        this.duree_jours = duree_jours;
    }

    public String getNom_voyage() {
        return nom_voyage;
    }

    public void setNom_voyage(String nom_voyage) {
        this.nom_voyage = nom_voyage;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public String getType_de_voyage() {
        return type_de_voyage;
    }

    public void setType_de_voyage(String type_de_voyage) {
        this.type_de_voyage = type_de_voyage;
    }

    public String getActivites_incluses() {
        return activites_incluses;
    }

    public void setActivites_incluses(String activites_incluses) {
        this.activites_incluses = activites_incluses;
    }

    public double getPrix() {
        return prix;
    }

    public void setPrix(double prix) {
        this.prix = prix;
    }

    public List<Trip> getTrips() {
        return trips;
    }

    public void setTrips(List<Trip> trips) {
        this.trips = trips;
    }
}

