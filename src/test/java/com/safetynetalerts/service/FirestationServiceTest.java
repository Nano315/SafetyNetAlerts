package com.safetynetalerts.service;

import com.safetynetalerts.dto.FirestationCoverageDTO;
import com.safetynetalerts.model.Firestation;
import com.safetynetalerts.model.MedicalRecord;
import com.safetynetalerts.model.Person;
import com.safetynetalerts.repository.DataRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class FirestationServiceTest {

    private DataRepository dataRepository;
    private FirestationService firestationService;

    // --- Test data ---
    private Person johnAdult;
    private Person rogerChild;
    private Person janeOther;

    private MedicalRecord mrJohn;
    private MedicalRecord mrRoger;
    private MedicalRecord mrJane;

    private List<Firestation> firestationMappings;

    @BeforeEach
    void setUp() {
        dataRepository = mock(DataRepository.class);
        firestationService = new FirestationService(dataRepository);

        // Persons ---
        johnAdult = new Person("John", "Boyd", "1509 Culver St", "Culver", "97451",
                "111-111", "john@email.com");
        rogerChild = new Person("Roger", "Boyd", "1509 Culver St", "Culver", "97451",
                "222-222", "roger@email.com");
        janeOther = new Person("Jane", "Doe", "29 15th St", "Culver", "97451",
                "333-333", "jane@email.com");

        // Medical records ---
        mrJohn = new MedicalRecord("John", "Boyd", "01/01/1980",
                List.of(), List.of()); // adult
        mrRoger = new MedicalRecord("Roger", "Boyd", "01/01/2015",
                List.of(), List.of()); // child
        mrJane = new MedicalRecord("Jane", "Doe", "02/02/1990",
                List.of("ibupurin:200mg"), List.of("pollen"));

        // Firestations ---
        Firestation fs1 = new Firestation("1509 Culver St", "1");
        Firestation fs2 = new Firestation("29 15th St", "2");
        firestationMappings = new ArrayList<>(List.of(fs1, fs2));

        // Stubs ---
        when(dataRepository.getPersons())
                .thenReturn(List.of(johnAdult, rogerChild, janeOther));
        when(dataRepository.getMedicalRecords())
                .thenReturn(List.of(mrJohn, mrRoger, mrJane));
        when(dataRepository.getFirestations())
                .thenAnswer(invocation -> firestationMappings); // return live list
    }

    // -------------------------------
    // getPersonsCoveredByStation
    // -------------------------------
    @Test
    void getPersonsCoveredByStation_returnsDtoWithCounts() {
        FirestationCoverageDTO dto = firestationService.getPersonsCoveredByStation("1");

        // 2 habitants (1 adulte, 1 enfant) pour la station 1
        assertThat(dto.getPersons()).hasSize(2);
        assertThat(dto.getNumberOfAdults()).isEqualTo(1);
        assertThat(dto.getNumberOfChildren()).isEqualTo(1);
        assertThat(dto.getPersons())
                .extracting("firstName")
                .containsExactlyInAnyOrder("John", "Roger");
    }

    // -------------------------------
    // addFirestation
    // -------------------------------
    @Test
    void addFirestation_whenAddressNew_addsMapping() {
        Firestation newFs = new Firestation("487 New St", "3");
        Firestation created = firestationService.addFirestation(newFs);

        assertThat(created).isNotNull();
        assertThat(firestationMappings).contains(newFs);
    }

    @Test
    void addFirestation_whenAddressExists_returnsNull() {
        Firestation duplicate = new Firestation("1509 Culver St", "9");
        Firestation created = firestationService.addFirestation(duplicate);

        assertThat(created).isNull();
        // taille inchangée
        assertThat(firestationMappings).hasSize(2);
    }

    // -------------------------------
    // updateFirestation
    // -------------------------------
    @Test
    void updateFirestation_whenAddressFound_updatesStation() {
        Firestation update = new Firestation("1509 Culver St", "5");
        Firestation updated = firestationService.updateFirestation(update);

        assertThat(updated).isNotNull();
        assertThat(updated.getStation()).isEqualTo("5");
    }

    @Test
    void updateFirestation_whenAddressNotFound_returnsNull() {
        Firestation update = new Firestation("Unknown", "7");
        Firestation updated = firestationService.updateFirestation(update);
        assertThat(updated).isNull();
    }

    // -------------------------------
    // deleteFirestation
    // -------------------------------
    @Test
    void deleteFirestation_whenAddressExists_returnsTrueAndRemoves() {
        boolean removed = firestationService.deleteFirestation("29 15th St");

        assertThat(removed).isTrue();
        assertThat(firestationMappings)
                .extracting(Firestation::getAddress)
                .doesNotContain("29 15th St");
    }

    @Test
    void deleteFirestation_whenAddressMissing_returnsFalse() {
        boolean removed = firestationService.deleteFirestation("No street");
        assertThat(removed).isFalse();
        assertThat(firestationMappings).hasSize(2);
    }
}
