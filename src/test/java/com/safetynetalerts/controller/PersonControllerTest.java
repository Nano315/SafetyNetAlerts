package com.safetynetalerts.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.safetynetalerts.model.Person;
import com.safetynetalerts.service.PersonService;
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

@WebMvcTest(PersonController.class)
public class PersonControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private PersonService personService;

    private Person john;
    private Person jane;

    @BeforeEach
    void setUp() {
        john = new Person("John", "Boyd", "1509 Culver St", "Culver", "97451", "111-111", "john@mail.com");
        jane = new Person("Jane", "Doe", "29 15th St", "Culver", "97451", "222-222", "jane@mail.com");
    }

    @Test
    void getAllPersons_returnsList() throws Exception {
        Mockito.when(personService.getAllPersons()).thenReturn(List.of(john, jane));

        mockMvc.perform(get("/person").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].firstName", is("John")))
                .andExpect(jsonPath("$[1].firstName", is("Jane")));
    }

    @Test
    void getPersonByName_found() throws Exception {
        Mockito.when(personService.getPersonByName("John", "Boyd")).thenReturn(john);

        mockMvc.perform(get("/person/search")
                .param("firstName", "John")
                .param("lastName", "Boyd"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email", is("john@mail.com")));
    }

    @Test
    void addPerson_createsPerson() throws Exception {
        Mockito.when(personService.addPerson(Mockito.any(Person.class))).thenReturn(john);

        mockMvc.perform(post("/person")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(john)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName", is("John")))
                .andExpect(jsonPath("$.city", is("Culver")));
    }

    @Test
    void updatePerson_updatesFields() throws Exception {
        Person updated = new Person("John", "Boyd", "new address", "NewCity", "00000", "999-999", "john@mail.com");
        Mockito.when(personService.updatePerson(Mockito.any(Person.class))).thenReturn(updated);

        mockMvc.perform(put("/person")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updated)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.address", is("new address")))
                .andExpect(jsonPath("$.city", is("NewCity")));
    }

    @Test
    void deletePerson_returnsOk() throws Exception {
        Mockito.when(personService.deletePerson("John", "Boyd")).thenReturn(true);

        mockMvc.perform(delete("/person")
                .param("firstName", "John")
                .param("lastName", "Boyd"))
                .andExpect(status().isOk());
    }
}
