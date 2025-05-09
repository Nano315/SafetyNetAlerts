package com.safetynetalerts.dto;

import java.util.List;

/**
 * Représente un enfant résidant à une adresse donnée, tel que retourné
 * par l’endpoint /childAlert.
 *
 * @param otherHouseMembers liste des autres occupants du foyer
 *                          (prénom + nom), adultes ou enfants.
 */
public class ChildAlertDTO {

    private String firstName;
    private String lastName;
    private int age;
    private List<String> otherHouseMembers;

    /* ------------------------------------------------------------------ */
    /* Constructeurs */
    /* ------------------------------------------------------------------ */

    /** Constructeur par défaut requis par Jackson. */
    public ChildAlertDTO() {
    }

    public ChildAlertDTO(String firstName,
            String lastName,
            int age,
            List<String> otherHouseMembers) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.age = age;
        this.otherHouseMembers = otherHouseMembers;
    }

    /* ------------------------------------------------------------------ */
    /* Accesseurs / Mutateurs */
    /* ------------------------------------------------------------------ */

    /** Prénom de l’enfant. */
    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /** Nom de l’enfant. */
    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    /** Âge (calculé côté service). */
    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    /** Autres membres du foyer (liste de chaînes « Prénom Nom »). */
    public List<String> getOtherHouseMembers() {
        return otherHouseMembers;
    }

    public void setOtherHouseMembers(List<String> otherHouseMembers) {
        this.otherHouseMembers = otherHouseMembers;
    }
}
