package com.safetynetalerts.dto;

import java.util.List;

/**
 * Réponse de l’endpoint /firestation?stationNumber=….
 * Contient :
 *
 * la liste des habitants (info minimale),
 * le nombre d’adultes,
 * le nombre d’enfants.
 *
 */
public class FirestationCoverageDTO {

    private List<PersonInfoDTO> persons;
    private int numberOfAdults;
    private int numberOfChildren;

    /** Constructeur par défaut (Jackson). */
    public FirestationCoverageDTO() {
    }

    public FirestationCoverageDTO(List<PersonInfoDTO> persons,
            int numberOfAdults,
            int numberOfChildren) {
        this.persons = persons;
        this.numberOfAdults = numberOfAdults;
        this.numberOfChildren = numberOfChildren;
    }

    /* ------------------------------------------------------------------ */
    /* Accesseurs / Mutateurs */
    /* ------------------------------------------------------------------ */

    /** Habitants couverts par la caserne. */
    public List<PersonInfoDTO> getPersons() {
        return persons;
    }

    public void setPersons(List<PersonInfoDTO> persons) {
        this.persons = persons;
    }

    /** Nombre d’adultes (> 18 ans). */
    public int getNumberOfAdults() {
        return numberOfAdults;
    }

    public void setNumberOfAdults(int numberOfAdults) {
        this.numberOfAdults = numberOfAdults;
    }

    /** Nombre d’enfants (=< 18 ans). */
    public int getNumberOfChildren() {
        return numberOfChildren;
    }

    public void setNumberOfChildren(int numberOfChildren) {
        this.numberOfChildren = numberOfChildren;
    }
}
