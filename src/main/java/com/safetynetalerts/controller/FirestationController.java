package com.safetynetalerts.controller;

import com.safetynetalerts.dto.FirestationCoverageDTO;
import com.safetynetalerts.model.Firestation;
import com.safetynetalerts.service.FirestationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/firestation")
public class FirestationController {

    private static final Logger logger = LoggerFactory.getLogger(FirestationController.class);

    private final FirestationService firestationService;

    public FirestationController(FirestationService firestationService) {
        this.firestationService = firestationService;
    }

    /**
     * GET /firestation?stationNumber=...
     * renvoie la liste des personnes + nb adultes + nb enfants
     */
    @GetMapping
    public FirestationCoverageDTO getPersonsByStation(@RequestParam String stationNumber) {
        logger.info("GET /firestation?stationNumber={}", stationNumber);
        FirestationCoverageDTO coverage = firestationService.getPersonsCoveredByStation(stationNumber);
        logger.info("Response => {} persons, {} adults, {} children",
                coverage.getPersons().size(), coverage.getNumberOfAdults(), coverage.getNumberOfChildren());
        return coverage;
    }

    /**
     * POST /firestation
     * ajoute un mapping (address + station)
     */
    @PostMapping
    public Firestation addFirestation(@RequestBody Firestation newMapping) {
        logger.info("POST /firestation => adding address={}, station={}",
                newMapping.getAddress(), newMapping.getStation());
        Firestation created = firestationService.addFirestation(newMapping);
        return created; // Si null, c'est qu'on a pas pu créer
    }

    /**
     * PUT /firestation
     * met à jour le numéro de station pour l'adresse
     */
    @PutMapping
    public Firestation updateFirestation(@RequestBody Firestation updatedMapping) {
        logger.info("PUT /firestation => address={}, new station={}",
                updatedMapping.getAddress(), updatedMapping.getStation());
        Firestation updated = firestationService.updateFirestation(updatedMapping);
        return updated; // Si null, c'est qu'on n'a pas trouvé l'adresse
    }

    /**
     * DELETE /firestation?address=...
     * supprime le mapping pour l'adresse
     */
    @DeleteMapping
    public void deleteFirestation(@RequestParam String address) {
        logger.info("DELETE /firestation?address={}", address);
        boolean removed = firestationService.deleteFirestation(address);
        if (!removed) {
            logger.warn("Mapping not found for address={}", address);
        }
    }
}
