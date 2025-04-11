package com.safetynetalerts.controller;

import com.safetynetalerts.model.Person;
import com.safetynetalerts.service.PersonService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/person")
public class PersonController {

    private static final Logger logger = LoggerFactory.getLogger(PersonController.class);

    private final PersonService personService;

    public PersonController(PersonService personService) {
        this.personService = personService;
    }

    /**
     * (Optionnel) GET /person
     * Récupère la liste complète des personnes (utile pour tester)
     */
    @GetMapping
    public List<Person> getAllPersons() {
        logger.info("GET /person called => fetching all persons");
        List<Person> persons = personService.getAllPersons();
        logger.info("Found {} persons.", persons.size());
        return persons;
    }

    /**
     * (Optionnel) GET /person/search?firstName=x&lastName=y
     * Récupère une personne par son prénom et nom
     */
    @GetMapping("/search")
    public Person getPersonByName(@RequestParam String firstName, @RequestParam String lastName) {
        logger.info("GET /person/search?firstName={}&lastName={} called", firstName, lastName);
        Person p = personService.getPersonByName(firstName, lastName);
        if (p == null) {
            logger.warn("Person not found: {} {}", firstName, lastName);
        }
        return p;
    }

    /**
     * POST /person
     * Ajoute une nouvelle personne
     */
    @PostMapping
    public Person addPerson(@RequestBody Person newPerson) {
        logger.info("POST /person => creating person: {} {}", newPerson.getFirstName(), newPerson.getLastName());
        Person created = personService.addPerson(newPerson);
        return created; // Null si la personne existe déjà
    }

    /**
     * PUT /person
     * Met à jour une personne existante
     * On recherche par (firstName, lastName) dans le corps.
     */
    @PutMapping
    public Person updatePerson(@RequestBody Person updatedPerson) {
        logger.info("PUT /person => updating person: {} {}", updatedPerson.getFirstName(), updatedPerson.getLastName());
        Person updated = personService.updatePerson(updatedPerson);
        return updated; // Null si la personne n'a pas été trouvée
    }

    /**
     * DELETE /person?firstName=x&lastName=y
     * Supprime la personne correspondant au couple (firstName, lastName)
     */
    @DeleteMapping
    public void deletePerson(@RequestParam String firstName, @RequestParam String lastName) {
        logger.info("DELETE /person?firstName={}&lastName={} called", firstName, lastName);
        boolean removed = personService.deletePerson(firstName, lastName);
        if (!removed) {
            logger.warn("No person found for firstName={}, lastName={}", firstName, lastName);
        }
    }
}
