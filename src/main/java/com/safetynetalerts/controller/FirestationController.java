package com.safetynetalerts.controller;

import com.safetynetalerts.dto.FirestationCoverageDTO;
import com.safetynetalerts.model.Firestation;
import com.safetynetalerts.service.FirestationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

/**
 * Contrôleur responsable des opérations CRUD sur les mappings
 * « adresse ⇄ numéro de caserne », ainsi que de l’endpoint de couverture.
 */
@RestController
@RequestMapping("/firestation")
public class FirestationController {

    /** Journalisation applicative. */
    private static final Logger logger = LoggerFactory.getLogger(FirestationController.class);

    private final FirestationService firestationService;

    public FirestationController(FirestationService firestationService) {
        this.firestationService = firestationService;
    }

    /**
     * Renvoie la liste des personnes couvertes par une caserne ainsi que
     * le nombre d’adultes et d’enfants.
     *
     * @param stationNumber numéro de la caserne recherché
     * @return DTO récapitulatif (personnes + compteurs)
     */
    @GetMapping
    public FirestationCoverageDTO getPersonsByStation(@RequestParam String stationNumber) {
        logger.info("GET /firestation?stationNumber={}", stationNumber);
        FirestationCoverageDTO coverage = firestationService.getPersonsCoveredByStation(stationNumber);
        logger.info("Réponse : {} pers. ({} adultes / {} enfants)",
                coverage.getPersons().size(),
                coverage.getNumberOfAdults(),
                coverage.getNumberOfChildren());
        return coverage;
    }

    /**
     * Ajoute un nouveau mapping caserne → adresse.
     *
     * @param newMapping objet JSON {address, station}
     * @return mapping créé ou {@code null} si l’adresse existe déjà
     */
    @PostMapping
    public Firestation addFirestation(@RequestBody Firestation newMapping) {
        logger.info("POST /firestation – address={}, station={}",
                newMapping.getAddress(), newMapping.getStation());
        return firestationService.addFirestation(newMapping);
    }

    /**
     * Modifie le numéro de caserne associé à une adresse existante.
     *
     * @param updatedMapping objet JSON {address, station}
     * @return mapping mis à jour ou {@code null} si l’adresse est inconnue
     */
    @PutMapping
    public Firestation updateFirestation(@RequestBody Firestation updatedMapping) {
        logger.info("PUT /firestation – address={}, newStation={}",
                updatedMapping.getAddress(), updatedMapping.getStation());
        return firestationService.updateFirestation(updatedMapping);
    }

    /**
     * Supprime le mapping caserne → adresse correspondant.
     *
     * @param address adresse à supprimer
     */
    @DeleteMapping
    public void deleteFirestation(@RequestParam String address) {
        logger.info("DELETE /firestation?address={}", address);
        boolean removed = firestationService.deleteFirestation(address);
        if (!removed) {
            logger.warn("Aucun mapping trouvé pour l’adresse {}", address);
        }
    }
}
