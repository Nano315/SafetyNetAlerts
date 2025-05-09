package com.safetynetalerts.service;

import com.safetynetalerts.dto.*;
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
import java.util.*;
import java.util.stream.Collectors;

@Service
public class AlertService {

    private static final Logger logger = LoggerFactory.getLogger(AlertService.class);

    private final DataRepository dataRepository;
    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");

    public AlertService(DataRepository dataRepository) {
        this.dataRepository = dataRepository;
    }

    /**
     * GET /childAlert?address=<address>
     * Retourne la liste des enfants vivant à l'adresse (=< 18 ans) avec leur âge,
     * plus la liste des autres membres du foyer (prénom + nom).
     * S'il n'y a pas d'enfant => liste vide (ou chaîne vide).
     */
    public List<ChildAlertDTO> getChildAlert(String address) {
        // 1) Trouver toutes les personnes à cette adresse
        List<Person> personsAtAddress = dataRepository.getPersons().stream()
                .filter(p -> p.getAddress().equalsIgnoreCase(address))
                .collect(Collectors.toList());

        // 2) Séparer enfants / adultes
        List<Person> children = new ArrayList<>();
        for (Person p : personsAtAddress) {
            MedicalRecord mr = findMedicalRecord(p.getFirstName(), p.getLastName());
            if (mr != null) {
                int age = calculateAge(mr.getBirthdate());
                if (age <= 18) {
                    children.add(p);
                }
            }
        }

        // 3) Construire la liste ChildAlertDTO pour chaque enfant
        List<ChildAlertDTO> result = new ArrayList<>();
        for (Person child : children) {
            MedicalRecord childMr = findMedicalRecord(child.getFirstName(), child.getLastName());
            int childAge = calculateAge(childMr.getBirthdate());

            // Liste des autres membres du foyer (adulte ou enfant), sauf l'enfant lui-même
            List<String> otherHouseMembers = new ArrayList<>();
            for (Person p : personsAtAddress) {
                if (!p.equals(child)) {
                    otherHouseMembers.add(p.getFirstName() + " " + p.getLastName());
                }
            }

            ChildAlertDTO dto = new ChildAlertDTO(child.getFirstName(), child.getLastName(),
                    childAge, otherHouseMembers);
            result.add(dto);
        }

        logger.debug("childAlert for address={} => {} children", address, result.size());
        return result;
    }

    /**
     * GET /phoneAlert?firestation=<station_number>
     * Retourne la liste des numéros de téléphone des résidents couverts par la
     * station.
     */
    public List<String> getPhoneAlert(String stationNumber) {
        // 1) Trouver les adresses desservies par cette station
        List<String> addresses = dataRepository.getFirestations().stream()
                .filter(f -> f.getStation().equals(stationNumber))
                .map(Firestation::getAddress)
                .collect(Collectors.toList());

        // 2) Récupérer les personnes vivant à ces adresses, extraire leurs numéros
        Set<String> phoneNumbers = dataRepository.getPersons().stream()
                .filter(p -> addresses.contains(p.getAddress()))
                .map(Person::getPhone)
                .collect(Collectors.toSet()); // set pour éviter doublons

        logger.debug("phoneAlert for station={} => {} phone numbers", stationNumber, phoneNumbers.size());
        return new ArrayList<>(phoneNumbers);
    }

    /**
     * GET /fire?address=<address>
     * Retourne la liste des habitants vivant à l'adresse + la station de pompiers
     * desservant cette adresse.
     * Pour chaque habitant: nom, téléphone, âge, médicaments, allergies.
     */
    public FireDTO getFire(String address) {
        // 1) Trouver la stationNumber associée à cette adresse (il peut y avoir
        // plusieurs mappings, on en prend un)
        String stationNumber = findStationNumberByAddress(address);

        // 2) Récupérer les personnes vivant à cette adresse
        List<Person> personsAtAddress = dataRepository.getPersons().stream()
                .filter(p -> p.getAddress().equalsIgnoreCase(address))
                .collect(Collectors.toList());

        // 3) Construire la liste FirePersonDTO
        List<FirePersonDTO> personDTOs = new ArrayList<>();
        for (Person p : personsAtAddress) {
            MedicalRecord mr = findMedicalRecord(p.getFirstName(), p.getLastName());
            if (mr != null) {
                int age = calculateAge(mr.getBirthdate());
                FirePersonDTO fpd = new FirePersonDTO(
                        p.getFirstName(),
                        p.getLastName(),
                        p.getPhone(),
                        age,
                        mr.getMedications(),
                        mr.getAllergies());
                personDTOs.add(fpd);
            }
        }

        logger.debug("fire for address={} => station={}, persons={}", address, stationNumber, personDTOs.size());
        return new FireDTO(stationNumber, personDTOs);
    }

    /**
     * GET /flood/stations?stations=<list_of_station_numbers>
     * Retourne une liste de foyers (adresse + occupants).
     * Chaque occupant: nom, téléphone, âge, meds, allergies
     */
    public List<HouseholdDTO> getFloodStations(List<String> stationNumbers) {
        // 1) Trouver toutes les adresses desservies par ces stations
        Set<String> addresses = dataRepository.getFirestations().stream()
                .filter(f -> stationNumbers.contains(f.getStation()))
                .map(Firestation::getAddress)
                .collect(Collectors.toSet());

        // 2) Pour chaque adresse, lister les occupants
        List<HouseholdDTO> result = new ArrayList<>();

        for (String address : addresses) {
            List<Person> personsAtAddress = dataRepository.getPersons().stream()
                    .filter(p -> p.getAddress().equalsIgnoreCase(address))
                    .collect(Collectors.toList());

            List<OccupantDTO> occupants = new ArrayList<>();
            for (Person p : personsAtAddress) {
                MedicalRecord mr = findMedicalRecord(p.getFirstName(), p.getLastName());
                if (mr != null) {
                    int age = calculateAge(mr.getBirthdate());
                    OccupantDTO odto = new OccupantDTO(
                            p.getFirstName(),
                            p.getLastName(),
                            age,
                            p.getPhone(),
                            mr.getMedications(),
                            mr.getAllergies());
                    occupants.add(odto);
                }
            }
            if (!occupants.isEmpty()) {
                HouseholdDTO household = new HouseholdDTO(address, occupants);
                result.add(household);
            }
        }

        logger.debug("floodStations for stations={} => {} households", stationNumbers, result.size());
        return result;
    }

    /**
     * GET /personInfo?lastName=<lastName>
     * Retourne nom, adresse, âge, email et antécédents médicaux pour chaque
     * habitant portant ce nom.
     */
    public List<PersonInfoDetailsDTO> getPersonInfo(String lastName) {
        // Trouver toutes les personnes ayant ce lastName
        List<Person> matchingPersons = dataRepository.getPersons().stream()
                .filter(p -> p.getLastName().equalsIgnoreCase(lastName))
                .collect(Collectors.toList());

        List<PersonInfoDetailsDTO> result = new ArrayList<>();
        for (Person p : matchingPersons) {
            MedicalRecord mr = findMedicalRecord(p.getFirstName(), p.getLastName());
            if (mr != null) {
                int age = calculateAge(mr.getBirthdate());
                PersonInfoDetailsDTO dto = new PersonInfoDetailsDTO(
                        p.getFirstName(),
                        p.getLastName(),
                        p.getAddress(),
                        age,
                        p.getEmail(),
                        mr.getMedications(),
                        mr.getAllergies());
                result.add(dto);
            }
        }

        logger.debug("personInfo for lastName={} => {} persons", lastName, result.size());
        return result;
    }

    /**
     * GET /communityEmail?city=<city>
     * Retourne la liste des e-mails de tous les habitants de la ville.
     */
    public List<String> getCommunityEmail(String city) {
        // Récupérer toutes les personnes habitant la ville, en extraire les e-mails
        Set<String> emails = dataRepository.getPersons().stream()
                .filter(p -> p.getCity().equalsIgnoreCase(city))
                .map(Person::getEmail)
                .collect(Collectors.toSet());

        logger.debug("communityEmail for city={} => {} emails", city, emails.size());
        return new ArrayList<>(emails);
    }

    // ----------------------------------------------------------------------
    // Méthodes utilitaires privées
    // ----------------------------------------------------------------------

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

    private String findStationNumberByAddress(String address) {
        // On peut avoir plusieurs mappings pour la même adresse, on en récupère un
        Optional<Firestation> fs = dataRepository.getFirestations().stream()
                .filter(f -> f.getAddress().equalsIgnoreCase(address))
                .findFirst();
        return fs.map(Firestation::getStation).orElse("");
    }
}
