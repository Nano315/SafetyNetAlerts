package com.safetynetalerts.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.safetynetalerts.model.MedicalRecord;
import com.safetynetalerts.service.MedicalRecordService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(MedicalRecordController.class)
public class MedicalRecordControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private MedicalRecordService medicalRecordService;

    private MedicalRecord johnRec;
    private MedicalRecord janeRec;

    @BeforeEach
    void setUp() {
        johnRec = new MedicalRecord("John", "Boyd", "01/01/1980",
                List.of("aznol:200mg"), List.of("nillacilan"));
        janeRec = new MedicalRecord("Jane", "Doe", "02/02/1990",
                List.of(), List.of());
    }

    @Test
    void getAllMedicalRecords_returnsList() throws Exception {
        Mockito.when(medicalRecordService.getAllMedicalRecords()).thenReturn(List.of(johnRec, janeRec));

        mockMvc.perform(get("/medicalRecord").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].firstName", is("John")))
                .andExpect(jsonPath("$[1].firstName", is("Jane")));
    }

    @Test
    void getMedicalRecordByName_found() throws Exception {
        Mockito.when(medicalRecordService.getMedicalRecordByName("John", "Boyd")).thenReturn(johnRec);

        mockMvc.perform(get("/medicalRecord/search")
                .param("firstName", "John")
                .param("lastName", "Boyd"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.birthdate", is("01/01/1980")));
    }

    @Test
    void addMedicalRecord_createsRecord() throws Exception {
        Mockito.when(medicalRecordService.addMedicalRecord(Mockito.any(MedicalRecord.class))).thenReturn(johnRec);

        mockMvc.perform(post("/medicalRecord")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(johnRec)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName", is("John")))
                .andExpect(jsonPath("$.medications", hasSize(1)));
    }

    @Test
    void updateMedicalRecord_updatesFields() throws Exception {
        MedicalRecord updated = new MedicalRecord("John", "Boyd", "03/03/1985",
                List.of("newmed:100mg"), List.of());
        Mockito.when(medicalRecordService.updateMedicalRecord(Mockito.any(MedicalRecord.class))).thenReturn(updated);

        mockMvc.perform(put("/medicalRecord")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updated)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.birthdate", is("03/03/1985")))
                .andExpect(jsonPath("$.medications[0]", is("newmed:100mg")));
    }

    @Test
    void deleteMedicalRecord_returnsOk() throws Exception {
        Mockito.when(medicalRecordService.deleteMedicalRecord("John", "Boyd")).thenReturn(true);

        mockMvc.perform(delete("/medicalRecord")
                .param("firstName", "John")
                .param("lastName", "Boyd"))
                .andExpect(status().isOk());
    }
}
