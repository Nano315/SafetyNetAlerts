package com.safetynetalerts.model;

import java.util.List;

/**
 * Représente un dossier médical de la base JSON.
 */
public class MedicalRecord {

    private String firstName;
    private String lastName;
    private String birthdate; // format MM/dd/yyyy
    private List<String> medications; // « nom:posologie »
    private List<String> allergies;

    /** Constructeur par défaut (Jackson). */
    public MedicalRecord() {
    }

    public MedicalRecord(String firstName,
            String lastName,
            String birthdate,
            List<String> medications,
            List<String> allergies) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.birthdate = birthdate;
        this.medications = medications;
        this.allergies = allergies;
    }

    /* ------------------------------------------------------------------ */
    /* Accesseurs / Mutateurs */
    /* ------------------------------------------------------------------ */

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getBirthdate() {
        return birthdate;
    }

    public void setBirthdate(String birthdate) {
        this.birthdate = birthdate;
    }

    public List<String> getMedications() {
        return medications;
    }

    public void setMedications(List<String> medications) {
        this.medications = medications;
    }

    public List<String> getAllergies() {
        return allergies;
    }

    public void setAllergies(List<String> allergies) {
        this.allergies = allergies;
    }
}
