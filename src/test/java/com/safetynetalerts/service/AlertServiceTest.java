package com.safetynetalerts.service;

import com.safetynetalerts.dto.*;
import com.safetynetalerts.model.Firestation;
import com.safetynetalerts.model.MedicalRecord;
import com.safetynetalerts.model.Person;
import com.safetynetalerts.repository.DataRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class AlertServiceTest {

    private DataRepository dataRepository;
    private AlertService alertService;

    // --- Test data ---
    private Person johnAdult;
    private Person rogerChild;
    private Person janeOther;

    private MedicalRecord mrJohn;
    private MedicalRecord mrRoger;
    private MedicalRecord mrJane;

    @BeforeEach
    void setUp() {
        dataRepository = mock(DataRepository.class);
        alertService = new AlertService(dataRepository);

        // Persons ---
        johnAdult = new Person("John", "Boyd", "1509 Culver St", "Culver", "97451",
                "111-111", "john@email.com");
        rogerChild = new Person("Roger", "Boyd", "1509 Culver St", "Culver", "97451",
                "222-222", "roger@email.com");
        janeOther = new Person("Jane", "Doe", "29 15th St", "Culver", "97451",
                "333-333", "jane@email.com");

        // Medical records ---
        mrJohn = new MedicalRecord("John", "Boyd", "01/01/1980",
                List.of(), List.of()); // >18
        mrRoger = new MedicalRecord("Roger", "Boyd", "01/01/2015",
                List.of(), List.of()); // child
        mrJane = new MedicalRecord("Jane", "Doe", "02/02/1990",
                List.of("ibupurin:200mg"), List.of("pollen"));

        // Firestations ---
        Firestation fs1 = new Firestation("1509 Culver St", "1");
        Firestation fs2 = new Firestation("29 15th St", "2");

        // Stub repository ---
        when(dataRepository.getPersons())
                .thenReturn(List.of(johnAdult, rogerChild, janeOther));
        when(dataRepository.getMedicalRecords())
                .thenReturn(List.of(mrJohn, mrRoger, mrJane));
        when(dataRepository.getFirestations())
                .thenReturn(List.of(fs1, fs2));
    }

    // -------------
    // childAlert
    // -------------
    @Test
    void getChildAlert_returnsChildAndOtherMembers() {
        List<ChildAlertDTO> children = alertService.getChildAlert("1509 Culver St");

        assertThat(children).hasSize(1);
        ChildAlertDTO child = children.get(0);
        assertThat(child.getFirstName()).isEqualTo("Roger");
        assertThat(child.getAge()).isLessThanOrEqualTo(18);
        assertThat(child.getOtherHouseMembers())
                .containsExactly("John Boyd");
    }

    @Test
    void getChildAlert_noChildren_returnsEmptyList() {
        List<ChildAlertDTO> children = alertService.getChildAlert("29 15th St");
        assertThat(children).isEmpty();
    }

    // -------------
    // phoneAlert
    // -------------
    @Test
    void getPhoneAlert_returnsDistinctPhones() {
        List<String> phones = alertService.getPhoneAlert("1");
        assertThat(phones).containsExactlyInAnyOrder("111-111", "222-222");
    }

    // -------
    // fire
    // -------
    @Test
    void getFire_returnsStationNumberAndOccupants() {
        FireDTO fireDTO = alertService.getFire("1509 Culver St");

        assertThat(fireDTO.getStationNumber()).isEqualTo("1");
        assertThat(fireDTO.getPersons()).hasSize(2);
        assertThat(fireDTO.getPersons())
                .extracting(FirePersonDTO::getFirstName)
                .containsExactlyInAnyOrder("John", "Roger");
    }

    // -----------------
    // flood/stations
    // -----------------
    @Test
    void getFloodStations_returnsHouseholdsByStation() {
        List<HouseholdDTO> households = alertService.getFloodStations(List.of("1"));
        assertThat(households).hasSize(1);
        HouseholdDTO house = households.get(0);
        assertThat(house.getAddress()).isEqualTo("1509 Culver St");
        assertThat(house.getOccupants()).hasSize(2);
    }

    // -------------
    // personInfo
    // -------------
    @Test
    void getPersonInfo_returnsAllPersonsWithLastName() {
        List<PersonInfoDetailsDTO> infos = alertService.getPersonInfo("Boyd");
        assertThat(infos).hasSize(2);
        assertThat(infos)
                .extracting(PersonInfoDetailsDTO::getFirstName)
                .containsExactlyInAnyOrder("John", "Roger");
    }

    // -----------------
    // communityEmail
    // -----------------
    @Test
    void getCommunityEmail_returnsEmailsForCity() {
        List<String> emails = alertService.getCommunityEmail("Culver");
        assertThat(emails).containsExactlyInAnyOrder(
                "john@email.com",
                "roger@email.com",
                "jane@email.com");
    }
}
