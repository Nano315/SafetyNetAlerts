package com.safetynetalerts.dto;

import java.util.List;

/**
 * Représente un foyer (adresse + occupants) dans la réponse de
 * /flood/stations.
 */
public class HouseholdDTO {

    private String address;
    private List<OccupantDTO> occupants;

    /** Constructeur par défaut requis par Jackson. */
    public HouseholdDTO() {
    }

    public HouseholdDTO(String address, List<OccupantDTO> occupants) {
        this.address = address;
        this.occupants = occupants;
    }

    /* ------------------------------------------------------------------ */
    /* Accesseurs / Mutateurs */
    /* ------------------------------------------------------------------ */

    /** Adresse du foyer. */
    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    /** Liste des occupants (détails). */
    public List<OccupantDTO> getOccupants() {
        return occupants;
    }

    public void setOccupants(List<OccupantDTO> occupants) {
        this.occupants = occupants;
    }
}
