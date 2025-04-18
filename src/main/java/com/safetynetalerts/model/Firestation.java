package com.safetynetalerts.model;

public class Firestation {
    private String address;
    private String station;

    public Firestation() {
        // Constructeur par défaut requis pour la sérialisation/désérialisation JSON
    }

    public Firestation(String address, String station) {
        this.address = address;
        this.station = station;
    }

    // --- Getters and Setters ---
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