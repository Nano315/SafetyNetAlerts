package com.safetynetalerts.service;

import com.safetynetalerts.model.Person;
import com.safetynetalerts.repository.DataRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

public class PersonServiceTest {

    private DataRepository dataRepository;
    private PersonService personService;

    private List<Person> personList;

    private Person bob;
    private Person alice;

    @BeforeEach
    void setUp() {
        dataRepository = mock(DataRepository.class);
        personService = new PersonService(dataRepository);

        bob = new Person("Bob", "Marley", "12 Jamaican St", "Kingston", "12345", "111", "bob@music.com");
        alice = new Person("Alice", "Cooper", "13 Rock St", "Phoenix", "54321", "222", "alice@rock.com");

        personList = new ArrayList<>(List.of(bob, alice));
        when(dataRepository.getPersons()).thenReturn(personList);
    }

    // getAllPersons ---
    @Test
    void getAllPersons_returnsCompleteList() {
        List<Person> all = personService.getAllPersons();
        assertThat(all).hasSize(2);
    }

    // getPersonByName ---
    @Test
    void getPersonByName_found() {
        Person p = personService.getPersonByName("Bob", "Marley");
        assertThat(p).isNotNull();
        assertThat(p.getEmail()).isEqualTo("bob@music.com");
    }

    @Test
    void getPersonByName_notFound_returnsNull() {
        Person p = personService.getPersonByName("John", "Doe");
        assertThat(p).isNull();
    }

    // addPerson ---
    @Test
    void addPerson_success() {
        Person newP = new Person("John", "Doe", "1 Main", "City", "00000", "333", "john@doe.com");
        Person created = personService.addPerson(newP);
        assertThat(created).isEqualTo(newP);
        assertThat(personList).contains(newP);
    }

    @Test
    void addPerson_duplicate_returnsNull() {
        Person dup = new Person("Bob", "Marley", "some", "C", "0", "999", "dup@dup.com");
        Person created = personService.addPerson(dup);
        assertThat(created).isNull();
        assertThat(personList).hasSize(2);
    }

    // updatePerson ---
    @Test
    void updatePerson_success() {
        Person update = new Person("Alice", "Cooper", "Updated Addr", "LA", "99999", "555", "new@alice.com");
        Person updated = personService.updatePerson(update);
        assertThat(updated).isNotNull();
        assertThat(updated.getAddress()).isEqualTo("Updated Addr");
        assertThat(updated.getEmail()).isEqualTo("new@alice.com");
    }

    @Test
    void updatePerson_notFound_returnsNull() {
        Person update = new Person("Ghost", "User", "x", "x", "x", "x", "x");
        Person result = personService.updatePerson(update);
        assertThat(result).isNull();
    }

    // deletePerson ---
    @Test
    void deletePerson_success_thenFail() {
        boolean removed = personService.deletePerson("Bob", "Marley");
        assertThat(removed).isTrue();
        assertThat(personList).doesNotContain(bob);

        // second appel : déjà supprimé -> false
        boolean removedAgain = personService.deletePerson("Bob", "Marley");
        assertThat(removedAgain).isFalse();
    }
}
