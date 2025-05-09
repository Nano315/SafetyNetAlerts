package com.safetynetalerts.controller;

import com.safetynetalerts.model.Person;
import com.safetynetalerts.service.PersonService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Contrôleur exposant les opérations CRUD sur les personnes
 * ainsi que deux endpoints de consultation (liste complète / recherche).
 */
@RestController
@RequestMapping("/person")
public class PersonController {

    /** Journalisation applicative. */
    private static final Logger LOG = LoggerFactory.getLogger(PersonController.class);

    private final PersonService personService;

    public PersonController(PersonService personService) {
        this.personService = personService;
    }

    /**
     * GET /person : liste toutes les personnes.
     */
    @GetMapping
    public List<Person> getAllPersons() {
        LOG.info("GET /person – liste complète");
        List<Person> persons = personService.getAllPersons();
        LOG.info("{} personne(s) trouvée(s)", persons.size());
        return persons;
    }

    /**
     * GET /person/search : cherche une personne
     * par prénom + nom.
     *
     * @param firstName prénom
     * @param lastName  nom
     */
    @GetMapping("/search")
    public Person getPersonByName(@RequestParam String firstName,
            @RequestParam String lastName) {
        LOG.info("GET /person/search – {} {}", firstName, lastName);
        Person person = personService.getPersonByName(firstName, lastName);
        if (person == null) {
            LOG.warn("Aucune personne {} {}", firstName, lastName);
        }
        return person;
    }

    /**
     * POST /person : ajoute une nouvelle personne.
     *
     * @param newPerson personne à créer
     * @return personne créée ou {@code null} en cas de doublon
     */
    @PostMapping
    public Person addPerson(@RequestBody Person newPerson) {
        LOG.info("POST /person – création {}", newPerson.getFirstName());
        return personService.addPerson(newPerson);
    }

    /**
     * PUT /person : met à jour une personne existante.
     * L’identification se fait par couple <code>firstName/lastName</code>.
     *
     * @param updatedPerson nouvelle version de la personne
     * @return personne mise à jour ou {@code null} si introuvable
     */
    @PutMapping
    public Person updatePerson(@RequestBody Person updatedPerson) {
        LOG.info("PUT /person – mise à jour {}", updatedPerson.getFirstName());
        return personService.updatePerson(updatedPerson);
    }

    /**
     * DELETE /person : supprime une personne.
     *
     * @param firstName prénom
     * @param lastName  nom
     */
    @DeleteMapping
    public void deletePerson(@RequestParam String firstName,
            @RequestParam String lastName) {
        LOG.info("DELETE /person – {} {}", firstName, lastName);
        boolean removed = personService.deletePerson(firstName, lastName);
        if (!removed) {
            LOG.warn("Suppression impossible : {} {} introuvable", firstName, lastName);
        }
    }
}
