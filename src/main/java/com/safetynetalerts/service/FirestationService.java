package com.safetynetalerts.service;

import com.safetynetalerts.dto.FirestationCoverageDTO;
import com.safetynetalerts.dto.PersonInfoDTO;
import com.safetynetalerts.model.Firestation;
import com.safetynetalerts.model.MedicalRecord;
import com.safetynetalerts.model.Person;
import com.safetynetalerts.repository.DataRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FirestationService {

    private static final Logger logger = LoggerFactory.getLogger(FirestationService.class);

    private final DataRepository dataRepository;
    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");

    public FirestationService(DataRepository dataRepository) {
        this.dataRepository = dataRepository;
    }

    /**
     * GET /firestation?stationNumber={stationNumber}
     * 
     * @return la liste des personnes (prénom, nom, adresse, téléphone)
     *         + le nombre d'adultes et d'enfants couverts par cette caserne
     */
    public FirestationCoverageDTO getPersonsCoveredByStation(String stationNumber) {
        // 1) Récupérer toutes les adresses correspondant à cette station
        List<Firestation> matchingFirestations = dataRepository.getFirestations().stream()
                .filter(f -> f.getStation().equals(stationNumber))
                .collect(Collectors.toList());

        List<String> addresses = matchingFirestations.stream()
                .map(Firestation::getAddress)
                .collect(Collectors.toList());

        // 2) Récupérer toutes les personnes habitant à ces adresses
        List<Person> coveredPersons = dataRepository.getPersons().stream()
                .filter(p -> addresses.contains(p.getAddress()))
                .collect(Collectors.toList());

        // 3) Construire la liste de PersonInfoDTO
        List<PersonInfoDTO> personsInfo = new ArrayList<>();
        for (Person person : coveredPersons) {
            personsInfo.add(new PersonInfoDTO(
                    person.getFirstName(),
                    person.getLastName(),
                    person.getAddress(),
                    person.getPhone()));
        }

        // 4) Compter enfants / adultes via birthdate
        int numberOfChildren = 0;
        int numberOfAdults = 0;
        for (Person person : coveredPersons) {
            MedicalRecord mr = findMedicalRecord(person.getFirstName(), person.getLastName());
            if (mr != null) {
                int age = calculateAge(mr.getBirthdate());
                if (age <= 18) {
                    numberOfChildren++;
                } else {
                    numberOfAdults++;
                }
            }
        }

        FirestationCoverageDTO dto = new FirestationCoverageDTO(personsInfo, numberOfAdults, numberOfChildren);
        logger.debug("Coverage for station {} => {} persons, {} adults, {} children",
                stationNumber, personsInfo.size(), numberOfAdults, numberOfChildren);

        return dto;
    }

    /**
     * Ajoute une nouvelle mapping firestation/address
     * (POST /firestation)
     */
    public Firestation addFirestation(Firestation newMapping) {
        // Vérifier si l'adresse n'existe pas déjà
        boolean alreadyExists = dataRepository.getFirestations().stream()
                .anyMatch(f -> f.getAddress().equalsIgnoreCase(newMapping.getAddress()));

        if (!alreadyExists) {
            dataRepository.getFirestations().add(newMapping);
            dataRepository.saveData();
            logger.info("Added new firestation mapping: address={}, station={}",
                    newMapping.getAddress(), newMapping.getStation());
            return newMapping;
        } else {
            logger.warn("Address {} already mapped to a station. No creation done.", newMapping.getAddress());
            return null; // ou lever une exception, selon votre choix
        }
    }

    /**
     * Met à jour le numéro de station pour une adresse existante
     * (PUT /firestation)
     */
    public Firestation updateFirestation(Firestation updatedMapping) {
        // Trouver la Firestation par adresse
        for (Firestation f : dataRepository.getFirestations()) {
            if (f.getAddress().equalsIgnoreCase(updatedMapping.getAddress())) {
                logger.info("Updating firestation for address={} from station={} to station={}",
                        f.getAddress(), f.getStation(), updatedMapping.getStation());
                f.setStation(updatedMapping.getStation());
                dataRepository.saveData();
                return f;
            }
        }
        logger.warn("Address {} not found, update not performed.", updatedMapping.getAddress());
        return null; // ou lever une exception, selon votre choix
    }

    /**
     * Supprime le mapping entre une station et une adresse
     * (DELETE /firestation?address=...)
     */
    public boolean deleteFirestation(String address) {
        List<Firestation> firestations = dataRepository.getFirestations();
        boolean removed = firestations.removeIf(f -> f.getAddress().equalsIgnoreCase(address));
        if (removed) {
            dataRepository.saveData();
            logger.info("Deleted firestation mapping for address={}", address);
        } else {
            logger.warn("No firestation mapping found for address={}", address);
        }
        return removed;
    }

    // Méthodes utilitaires internes

    private MedicalRecord findMedicalRecord(String firstName, String lastName) {
        return dataRepository.getMedicalRecords().stream()
                .filter(m -> m.getFirstName().equalsIgnoreCase(firstName)
                        && m.getLastName().equalsIgnoreCase(lastName))
                .findFirst()
                .orElse(null);
    }

    private int calculateAge(String birthdate) {
        LocalDate birthDate = LocalDate.parse(birthdate, dateFormatter);
        return Period.between(birthDate, LocalDate.now()).getYears();
    }
}
