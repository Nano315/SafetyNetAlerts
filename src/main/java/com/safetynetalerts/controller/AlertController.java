package com.safetynetalerts.controller;

import com.safetynetalerts.dto.*;
import com.safetynetalerts.service.AlertService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Contrôleur exposant les endpoints « alertes » décrits dans le cahier des
 * charges.
 * 
 * Il délègue toute la logique métier à {@link AlertService} et se contente
 * de :
 * journaliser la requête entrante ;
 * appeler le service ;
 * retourner la réponse au format JSON.
 * 
 */
@RestController
public class AlertController {

    /** Journalisation applicative. */
    private static final Logger logger = LoggerFactory.getLogger(AlertController.class);

    private final AlertService alertService;

    public AlertController(AlertService alertService) {
        this.alertService = alertService;
    }

    /**
     * Retourne la liste des enfants (=< 18 ans) résidant à l’adresse fournie,
     * ainsi que les autres membres du foyer.
     *
     * @param address adresse recherchée (ex. : « 1509 Culver St »)
     * @return liste éventuelle d’enfants, vide s’il n’y en a pas
     */
    @GetMapping("/childAlert")
    public List<ChildAlertDTO> getChildAlert(@RequestParam String address) {
        logger.info("GET /childAlert?address={}", address);
        List<ChildAlertDTO> children = alertService.getChildAlert(address);
        logger.info("Réponse : {} enfant(s)", children.size());
        return children;
    }

    /**
     * Retourne tous les numéros de téléphone desservis par la caserne indiquée.
     *
     * @param firestation numéro de caserne
     * @return liste de numéros uniques
     */
    @GetMapping("/phoneAlert")
    public List<String> getPhoneAlert(@RequestParam String firestation) {
        logger.info("GET /phoneAlert?firestation={}", firestation);
        List<String> phones = alertService.getPhoneAlert(firestation);
        logger.info("Réponse : {} numéro(s)", phones.size());
        return phones;
    }

    /**
     * Fournit les occupants d’une adresse ainsi que le numéro de caserne associé.
     *
     * @param address adresse recherchée
     * @return informations agrégées (occupants + station)
     */
    @GetMapping("/fire")
    public FireDTO getFire(@RequestParam String address) {
        logger.info("GET /fire?address={}", address);
        FireDTO fireResponse = alertService.getFire(address);
        logger.info("Station {} – {} occupant(s)", fireResponse.getStationNumber(),
                fireResponse.getPersons().size());
        return fireResponse;
    }

    /**
     * Retourne, pour une liste de casernes, les foyers à évacuer
     * (occupants groupés par adresse).
     *
     * @param stations liste des numéros de caserne (query string : 1,2,3…)
     * @return liste de foyers contenant occupants et informations médicales
     */
    @GetMapping("/flood/stations")
    public List<HouseholdDTO> getFloodStations(@RequestParam List<String> stations) {
        logger.info("GET /flood/stations?stations={}", stations);
        List<HouseholdDTO> households = alertService.getFloodStations(stations);
        logger.info("Réponse : {} foyer(s)", households.size());
        return households;
    }

    /**
     * Retourne les informations détaillées de toutes les personnes
     * portant le nom de famille indiqué.
     *
     * @param lastName nom recherché
     * @return liste (éventuellement vide) d’informations personnelles
     */
    @GetMapping("/personInfo")
    public List<PersonInfoDetailsDTO> getPersonInfo(@RequestParam String lastName) {
        logger.info("GET /personInfo?lastName={}", lastName);
        List<PersonInfoDetailsDTO> personInfos = alertService.getPersonInfo(lastName);
        logger.info("Réponse : {} personne(s)", personInfos.size());
        return personInfos;
    }

    /**
     * Retourne toutes les adresses e‑mail des habitants d’une ville.
     *
     * @param city nom de la ville
     * @return liste d’e‑mails sans doublon
     */
    @GetMapping("/communityEmail")
    public List<String> getCommunityEmail(@RequestParam String city) {
        logger.info("GET /communityEmail?city={}", city);
        List<String> emails = alertService.getCommunityEmail(city);
        logger.info("Réponse : {} e‑mail(s)", emails.size());
        return emails;
    }
}
