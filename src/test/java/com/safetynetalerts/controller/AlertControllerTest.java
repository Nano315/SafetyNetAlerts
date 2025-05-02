package com.safetynetalerts.controller;

import com.safetynetalerts.dto.*;
import com.safetynetalerts.service.AlertService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AlertController.class)
public class AlertControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private AlertService alertService;

    ChildAlertDTO child;
    FireDTO fireDto;
    HouseholdDTO household;
    PersonInfoDetailsDTO info1;

    @BeforeEach
    void init() {
        child = new ChildAlertDTO("Roger", "Boyd", 8, List.of("John Boyd"));
        FirePersonDTO fp1 = new FirePersonDTO("John", "Boyd", "111", 45, List.of(), List.of());
        fireDto = new FireDTO("1", List.of(fp1));
        OccupantDTO occ = new OccupantDTO("John", "Boyd", 45, "111", List.of(), List.of());
        household = new HouseholdDTO("1509 Culver St", List.of(occ));
        info1 = new PersonInfoDetailsDTO("John", "Boyd", "1509 Culver St", 45, "john@mail.com", List.of(), List.of());
    }

    @Test
    void childAlert_returnsChildren() throws Exception {
        Mockito.when(alertService.getChildAlert("1509 Culver St"))
                .thenReturn(List.of(child));

        mockMvc.perform(get("/childAlert").param("address", "1509 Culver St"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].firstName", is("Roger")));
    }

    @Test
    void phoneAlert_returnsPhones() throws Exception {
        Mockito.when(alertService.getPhoneAlert("1"))
                .thenReturn(List.of("111-111", "222-222"));

        mockMvc.perform(get("/phoneAlert").param("firestation", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)));
    }

    @Test
    void fire_returnsStationAndOccupants() throws Exception {
        Mockito.when(alertService.getFire("1509 Culver St")).thenReturn(fireDto);

        mockMvc.perform(get("/fire").param("address", "1509 Culver St"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.stationNumber", is("1")))
                .andExpect(jsonPath("$.persons", hasSize(1)));
    }

    @Test
    void floodStations_returnsHouseholds() throws Exception {
        Mockito.when(alertService.getFloodStations(List.of("1")))
                .thenReturn(List.of(household));

        mockMvc.perform(get("/flood/stations").param("stations", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].address", is("1509 Culver St")));
    }

    @Test
    void personInfo_returnsPersons() throws Exception {
        Mockito.when(alertService.getPersonInfo("Boyd"))
                .thenReturn(List.of(info1));

        mockMvc.perform(get("/personInfo").param("lastName", "Boyd"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].email", is("john@mail.com")));
    }

    @Test
    void communityEmail_returnsEmails() throws Exception {
        Mockito.when(alertService.getCommunityEmail("Culver"))
                .thenReturn(List.of("john@mail.com", "roger@mail.com"));

        mockMvc.perform(get("/communityEmail").param("city", "Culver"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)));
    }
}
