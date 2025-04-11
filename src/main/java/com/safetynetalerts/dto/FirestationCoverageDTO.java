package com.safetynetalerts.dto;

import java.util.List;

public class FirestationCoverageDTO {
    private List<PersonInfoDTO> persons;
    private int numberOfAdults;
    private int numberOfChildren;

    public FirestationCoverageDTO() {
        // Constructeur par défaut
    }

    public FirestationCoverageDTO(List<PersonInfoDTO> persons, int numberOfAdults, int numberOfChildren) {
        this.persons = persons;
        this.numberOfAdults = numberOfAdults;
        this.numberOfChildren = numberOfChildren;
    }

    // --- Getters & Setters ---
    public List<PersonInfoDTO> getPersons() {
        return persons;
    }

    public void setPersons(List<PersonInfoDTO> persons) {
        this.persons = persons;
    }

    public int getNumberOfAdults() {
        return numberOfAdults;
    }

    public void setNumberOfAdults(int numberOfAdults) {
        this.numberOfAdults = numberOfAdults;
    }

    public int getNumberOfChildren() {
        return numberOfChildren;
    }

    public void setNumberOfChildren(int numberOfChildren) {
        this.numberOfChildren = numberOfChildren;
    }
}
