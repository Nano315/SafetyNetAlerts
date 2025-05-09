package com.safetynetalerts.dto;

/**
 * Version allégée d’une personne : utilisée dans la réponse
 * /firestation?stationNumber=….
 */
public class PersonInfoDTO {

    private String firstName;
    private String lastName;
    private String address;
    private String phone;

    /** Constructeur par défaut (Jackson). */
    public PersonInfoDTO() {
    }

    public PersonInfoDTO(String firstName,
            String lastName,
            String address,
            String phone) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
        this.phone = phone;
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
