package com.safetynetalerts.dto;

import java.util.List;

/**
 * Détail d’un occupant pour la réponse des endpoints /fire
 * et /flood/stations.
 */
public class FirePersonDTO {

    private String firstName;
    private String lastName;
    private String phone;
    private int age;
    private List<String> medications;
    private List<String> allergies;

    /** Constructeur par défaut (Jackson). */
    public FirePersonDTO() {
    }

    public FirePersonDTO(String firstName,
            String lastName,
            String phone,
            int age,
            List<String> medications,
            List<String> allergies) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.phone = phone;
        this.age = age;
        this.medications = medications;
        this.allergies = allergies;
    }

    /* ------------------------------------------------------------------ */
    /* Accesseurs / Mutateurs */
    /* ------------------------------------------------------------------ */

    /** Prénom. */
    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /** Nom. */
    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    /** Numéro de téléphone. */
    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    /** Âge de la personne. */
    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    /** Liste des médicaments (format « nom:posologie »). */
    public List<String> getMedications() {
        return medications;
    }

    public void setMedications(List<String> medications) {
        this.medications = medications;
    }

    /** Liste des allergies connues. */
    public List<String> getAllergies() {
        return allergies;
    }

    public void setAllergies(List<String> allergies) {
        this.allergies = allergies;
    }
}
