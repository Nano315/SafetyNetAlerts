package com.safetynetalerts.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.safetynetalerts.dto.FirestationCoverageDTO;
import com.safetynetalerts.dto.PersonInfoDTO;
import com.safetynetalerts.model.Firestation;
import com.safetynetalerts.service.FirestationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(FirestationController.class)
public class FirestationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper mapper;

    @MockitoBean
    private FirestationService firestationService;

    private Firestation firestation;
    private Firestation updated;
    private FirestationCoverageDTO coverageDTO;

    @BeforeEach
    void init() {
        firestation = new Firestation("1509 Culver St", "1");
        updated = new Firestation("1509 Culver St", "5");
        List<PersonInfoDTO> persons = List.of(
                new PersonInfoDTO("John", "Boyd", "1509 Culver St", "111"),
                new PersonInfoDTO("Roger", "Boyd", "1509 Culver St", "222"));
        coverageDTO = new FirestationCoverageDTO(persons, 1, 1);
    }

    // GET /firestation
    @Test
    void getPersonsByStation_returnsCoverage() throws Exception {
        Mockito.when(firestationService.getPersonsCoveredByStation("1")).thenReturn(coverageDTO);

        mockMvc.perform(get("/firestation").param("stationNumber", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.persons", hasSize(2)))
                .andExpect(jsonPath("$.numberOfAdults", is(1)))
                .andExpect(jsonPath("$.numberOfChildren", is(1)));
    }

    // POST /firestation
    @Test
    void addFirestation_createsMapping() throws Exception {
        Mockito.when(firestationService.addFirestation(Mockito.any())).thenReturn(firestation);

        mockMvc.perform(post("/firestation")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(firestation)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.station", is("1")))
                .andExpect(jsonPath("$.address", is("1509 Culver St")));
    }

    // PUT /firestation
    @Test
    void updateFirestation_updatesMapping() throws Exception {
        Mockito.when(firestationService.updateFirestation(Mockito.any())).thenReturn(updated);

        mockMvc.perform(put("/firestation")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(updated)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.station", is("5")));
    }

    // DELETE /firestation
    @Test
    void deleteFirestation_returnsOk() throws Exception {
        Mockito.when(firestationService.deleteFirestation("1509 Culver St")).thenReturn(true);

        mockMvc.perform(delete("/firestation").param("address", "1509 Culver St"))
                .andExpect(status().isOk());
    }
}
