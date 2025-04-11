package com.safetynetalerts.dto;

import java.util.List;

public class HouseholdDTO {

    private String address;
    private List<OccupantDTO> occupants;

    // Constructeur par défaut
    public HouseholdDTO() {
    }

    // Constructeur paramétré
    public HouseholdDTO(String address, List<OccupantDTO> occupants) {
        this.address = address;
        this.occupants = occupants;
    }

    // --- Getters and Setters ---
    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public List<OccupantDTO> getOccupants() {
        return occupants;
    }

    public void setOccupants(List<OccupantDTO> occupants) {
        this.occupants = occupants;
    }
}
