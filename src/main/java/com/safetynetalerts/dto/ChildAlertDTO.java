package com.safetynetalerts.dto;

import java.util.List;

public class ChildAlertDTO {
    private String firstName;
    private String lastName;
    private int age;
    // Noms d’autres membres du foyer (adulte ou enfant), ex: "John Boyd", "Felicia
    // Boyd"
    private List<String> otherHouseMembers;

    public ChildAlertDTO() {
    }

    public ChildAlertDTO(String firstName, String lastName, int age, List<String> otherHouseMembers) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.age = age;
        this.otherHouseMembers = otherHouseMembers;
    }

    // Getters / Setters

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

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public List<String> getOtherHouseMembers() {
        return otherHouseMembers;
    }

    public void setOtherHouseMembers(List<String> otherHouseMembers) {
        this.otherHouseMembers = otherHouseMembers;
    }
}
