package com.example.comfelix_nath_anprojetx;

public class Reservation {

    private int id;
    private int idVoyage;
    private String destination;
    private String dateVoyage;
    private double montant;
    private String statut;


    public Reservation(int id, int idVoyage, String destination, String dateVoyage, double montant, String statut) {
        this.id = id;
        this.idVoyage = idVoyage;
        this.destination = destination;
        this.dateVoyage = dateVoyage;
        this.montant = montant;
        this.statut = statut;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdVoyage() {
        return idVoyage;
    }

    public void setIdVoyage(int idVoyage) {
        this.idVoyage = idVoyage;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getDateVoyage() {
        return dateVoyage;
    }

    public void setDateVoyage(String dateVoyage) {
        this.dateVoyage = dateVoyage;
    }

    public double getMontant() {
        return montant;
    }

    public void setMontant(double montant) {
        this.montant = montant;
    }

    public String getStatut() {
        return statut;
    }

    public void setStatut(String statut) {
        this.statut = statut;
    }
}

