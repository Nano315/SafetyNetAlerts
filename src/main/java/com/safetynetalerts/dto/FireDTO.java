package com.safetynetalerts.dto;

import java.util.List;

/**
 * Réponse de l’endpoint /fire : numéro de caserne desservant
 * l’adresse et liste détaillée des occupants.
 */
public class FireDTO {

    private String stationNumber;
    private List<FirePersonDTO> persons;

    /** Constructeur par défaut (Jackson). */
    public FireDTO() {
    }

    public FireDTO(String stationNumber, List<FirePersonDTO> persons) {
        this.stationNumber = stationNumber;
        this.persons = persons;
    }

    /* ------------------------------------------------------------------ */
    /* Accesseurs / Mutateurs */
    /* ------------------------------------------------------------------ */

    /** Numéro de la caserne de pompiers. */
    public String getStationNumber() {
        return stationNumber;
    }

    public void setStationNumber(String stationNumber) {
        this.stationNumber = stationNumber;
    }

    /** Liste des occupants (DTO détaillé). */
    public List<FirePersonDTO> getPersons() {
        return persons;
    }

    public void setPersons(List<FirePersonDTO> persons) {
        this.persons = persons;
    }
}
