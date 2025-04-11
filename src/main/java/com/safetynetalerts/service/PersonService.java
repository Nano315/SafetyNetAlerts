package com.safetynetalerts.service;

import com.safetynetalerts.model.Person;
import com.safetynetalerts.repository.DataRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PersonService {

    private static final Logger logger = LoggerFactory.getLogger(PersonService.class);

    private final DataRepository dataRepository;

    public PersonService(DataRepository dataRepository) {
        this.dataRepository = dataRepository;
    }

    /**
     * (Optionnel) Retourne la liste complète des personnes
     */
    public List<Person> getAllPersons() {
        return dataRepository.getPersons();
    }

    /**
     * (Optionnel) Retrouve une Person par son prénom et nom
     * Renvoie null si non trouvée
     */
    public Person getPersonByName(String firstName, String lastName) {
        Optional<Person> personOpt = dataRepository.getPersons().stream()
                .filter(p -> p.getFirstName().equalsIgnoreCase(firstName)
                        && p.getLastName().equalsIgnoreCase(lastName))
                .findFirst();
        return personOpt.orElse(null);
    }

    /**
     * Ajoute une nouvelle Person s'il n'y a pas déjà quelqu'un
     * avec le même firstName & lastName
     */
    public Person addPerson(Person newPerson) {
        boolean exists = dataRepository.getPersons().stream()
                .anyMatch(p -> p.getFirstName().equalsIgnoreCase(newPerson.getFirstName())
                        && p.getLastName().equalsIgnoreCase(newPerson.getLastName()));
        if (exists) {
            logger.warn("Person {} {} already exists. No creation performed.",
                    newPerson.getFirstName(), newPerson.getLastName());
            return null; // ou lever une exception en cas de duplication
        }

        dataRepository.getPersons().add(newPerson);
        logger.info("Added person: firstName={}, lastName={}, address={}, city={}, zip={}, phone={}, email={}",
                newPerson.getFirstName(),
                newPerson.getLastName(),
                newPerson.getAddress(),
                newPerson.getCity(),
                newPerson.getZip(),
                newPerson.getPhone(),
                newPerson.getEmail());
        return newPerson;
    }

    /**
     * Met à jour une Person existante.
     * On identifie la Person à partir du couple (firstName, lastName).
     * On modifie les autres champs si la Person est trouvée.
     */
    public Person updatePerson(Person updated) {
        for (Person p : dataRepository.getPersons()) {
            if (p.getFirstName().equalsIgnoreCase(updated.getFirstName())
                    && p.getLastName().equalsIgnoreCase(updated.getLastName())) {

                logger.info("Updating person: {} {}", p.getFirstName(), p.getLastName());
                p.setAddress(updated.getAddress());
                p.setCity(updated.getCity());
                p.setZip(updated.getZip());
                p.setPhone(updated.getPhone());
                p.setEmail(updated.getEmail());
                return p;
            }
        }
        logger.warn("Person {} {} not found. Update not performed.",
                updated.getFirstName(), updated.getLastName());
        return null;
    }

    /**
     * Supprime une Person (identifiée par firstName & lastName) de la liste.
     * Renvoie true si suppression effectuée, false si la personne n'a pas été
     * trouvée.
     */
    public boolean deletePerson(String firstName, String lastName) {
        List<Person> persons = dataRepository.getPersons();
        boolean removed = persons.removeIf(p -> p.getFirstName().equalsIgnoreCase(firstName)
                && p.getLastName().equalsIgnoreCase(lastName));
        if (removed) {
            logger.info("Deleted person: firstName={}, lastName={}", firstName, lastName);
        } else {
            logger.warn("Person {} {} not found. No deletion performed.", firstName, lastName);
        }
        return removed;
    }
}
