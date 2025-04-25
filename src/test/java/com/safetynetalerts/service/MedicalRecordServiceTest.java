package com.safetynetalerts.service;

import com.safetynetalerts.model.MedicalRecord;
import com.safetynetalerts.repository.DataRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

public class MedicalRecordServiceTest {

    private DataRepository dataRepository;
    private MedicalRecordService medicalRecordService;

    private List<MedicalRecord> records;
    private MedicalRecord mrJohn;
    private MedicalRecord mrJane;

    @BeforeEach
    void setUp() {
        dataRepository = mock(DataRepository.class);
        medicalRecordService = new MedicalRecordService(dataRepository);

        // jeu de données
        mrJohn = new MedicalRecord("John", "Boyd", "01/01/1984",
                List.of("aznol:200mg"), List.of("peanut"));
        mrJane = new MedicalRecord("Jane", "Doe", "02/02/1990",
                new ArrayList<>(), List.of("pollen"));

        records = new ArrayList<>(List.of(mrJohn, mrJane));
        when(dataRepository.getMedicalRecords()).thenReturn(records);
    }

    // --------------------
    // getAllMedicalRecords
    // --------------------
    @Test
    void getAllMedicalRecords_returnsFullList() {
        List<MedicalRecord> all = medicalRecordService.getAllMedicalRecords();
        assertThat(all).hasSize(2).containsExactlyInAnyOrder(mrJohn, mrJane);
    }

    // ----------------------
    // getMedicalRecordByName
    // ----------------------
    @Test
    void getMedicalRecordByName_found() {
        MedicalRecord found = medicalRecordService.getMedicalRecordByName("John", "Boyd");
        assertThat(found).isEqualTo(mrJohn);
    }

    @Test
    void getMedicalRecordByName_notFound_returnsNull() {
        MedicalRecord found = medicalRecordService.getMedicalRecordByName("Bob", "Marley");
        assertThat(found).isNull();
    }

    // -----------------
    // addMedicalRecord
    // -----------------
    @Test
    void addMedicalRecord_success() {
        MedicalRecord newRec = new MedicalRecord("Bob", "Marley", "03/03/1993",
                List.of("ganja:420mg"), List.of());
        MedicalRecord created = medicalRecordService.addMedicalRecord(newRec);

        assertThat(created).isEqualTo(newRec);
        assertThat(records).contains(newRec);
    }

    @Test
    void addMedicalRecord_duplicate_returnsNull() {
        MedicalRecord duplicate = new MedicalRecord("John", "Boyd", "01/01/1984",
                List.of(), List.of());
        MedicalRecord created = medicalRecordService.addMedicalRecord(duplicate);
        assertThat(created).isNull();
        assertThat(records).hasSize(2);
    }

    // --------------------
    // updateMedicalRecord
    // --------------------
    @Test
    void updateMedicalRecord_success() {
        MedicalRecord updated = new MedicalRecord("Jane", "Doe", "02/02/1991",
                List.of("ibupurin:200mg"), List.of());
        MedicalRecord result = medicalRecordService.updateMedicalRecord(updated);

        assertThat(result).isNotNull();
        assertThat(result.getBirthdate()).isEqualTo("02/02/1991");
        assertThat(result.getMedications()).containsExactly("ibupurin:200mg");
    }

    @Test
    void updateMedicalRecord_notFound_returnsNull() {
        MedicalRecord unknown = new MedicalRecord("Alice", "Wonder", "05/05/2000",
                List.of(), List.of());
        MedicalRecord result = medicalRecordService.updateMedicalRecord(unknown);
        assertThat(result).isNull();
    }

    // ---------------------
    // deleteMedicalRecord
    // ---------------------
    @Test
    void deleteMedicalRecord_success() {
        boolean removed = medicalRecordService.deleteMedicalRecord("John", "Boyd");
        assertThat(removed).isTrue();
        assertThat(records).doesNotContain(mrJohn);
    }

    @Test
    void deleteMedicalRecord_notFound_returnsFalse() {
        boolean removed = medicalRecordService.deleteMedicalRecord("Bob", "Marley");
        assertThat(removed).isFalse();
    }
}
