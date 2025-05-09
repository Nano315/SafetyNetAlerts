package com.safetynetalerts.model;

/**
 * Mapping « adresse <-> numéro de caserne » utilisé par l’application.
 * 
 * La classe contient uniquement les champs utiles au JSON fourni ;
 * aucune logique n’y est embarquée.
 */
public class Firestation {

    /** Adresse desservie. */
    private String address;

    /** Numéro de la caserne de pompiers. */
    private String station;

    /** Constructeur par défaut (Jackson). */
    public Firestation() {
    }

    public Firestation(String address, String station) {
        this.address = address;
        this.station = station;
    }

    /* ------------------------------------------------------------------ */
    /* Accesseurs / Mutateurs */
    /* ------------------------------------------------------------------ */

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getStation() {
        return station;
    }

    public void setStation(String station) {
        this.station = station;
    }
}
