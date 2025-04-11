package com.safetynetalerts.dto;

import java.util.List;

public class FireDTO {
    private String stationNumber;
    private List<FirePersonDTO> persons;

    public FireDTO() {
    }

    public FireDTO(String stationNumber, List<FirePersonDTO> persons) {
        this.stationNumber = stationNumber;
        this.persons = persons;
    }

    // Getters / Setters

    public String getStationNumber() {
        return stationNumber;
    }

    public void setStationNumber(String stationNumber) {
        this.stationNumber = stationNumber;
    }

    public List<FirePersonDTO> getPersons() {
        return persons;
    }

    public void setPersons(List<FirePersonDTO> persons) {
        this.persons = persons;
    }
}
