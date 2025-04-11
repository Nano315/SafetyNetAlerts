package com.safetynetalerts.controller;

import com.safetynetalerts.model.MedicalRecord;
import com.safetynetalerts.service.MedicalRecordService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/medicalRecord")
public class MedicalRecordController {

    private static final Logger logger = LoggerFactory.getLogger(MedicalRecordController.class);

    private final MedicalRecordService medicalRecordService;

    public MedicalRecordController(MedicalRecordService medicalRecordService) {
        this.medicalRecordService = medicalRecordService;
    }

    /**
     * (Optionnel) GET /medicalRecord
     * Liste tous les dossiers médicaux
     */
    @GetMapping
    public List<MedicalRecord> getAllMedicalRecords() {
        logger.info("GET /medicalRecord => fetching all medical records");
        List<MedicalRecord> allRecords = medicalRecordService.getAllMedicalRecords();
        logger.info("Found {} medical records.", allRecords.size());
        return allRecords;
    }

    /**
     * (Optionnel) GET /medicalRecord/search?firstName=x&lastName=y
     * Récupère un dossier médical précis
     */
    @GetMapping("/search")
    public MedicalRecord getMedicalRecordByName(@RequestParam String firstName, @RequestParam String lastName) {
        logger.info("GET /medicalRecord/search?firstName={}&lastName={}", firstName, lastName);
        MedicalRecord record = medicalRecordService.getMedicalRecordByName(firstName, lastName);
        if (record == null) {
            logger.warn("MedicalRecord not found for {} {}", firstName, lastName);
        }
        return record;
    }

    /**
     * POST /medicalRecord
     * Ajoute un nouveau dossier médical
     */
    @PostMapping
    public MedicalRecord addMedicalRecord(@RequestBody MedicalRecord newRecord) {
        logger.info("POST /medicalRecord => adding record for {} {}", newRecord.getFirstName(),
                newRecord.getLastName());
        MedicalRecord created = medicalRecordService.addMedicalRecord(newRecord);
        return created; // null si le dossier existe déjà
    }

    /**
     * PUT /medicalRecord
     * Met à jour un dossier médical existant (identifié par firstName/lastName)
     */
    @PutMapping
    public MedicalRecord updateMedicalRecord(@RequestBody MedicalRecord updatedRecord) {
        logger.info("PUT /medicalRecord => updating record for {} {}", updatedRecord.getFirstName(),
                updatedRecord.getLastName());
        MedicalRecord updated = medicalRecordService.updateMedicalRecord(updatedRecord);
        return updated; // null si le dossier n'a pas été trouvé
    }

    /**
     * DELETE /medicalRecord?firstName=x&lastName=y
     * Supprime un dossier médical
     */
    @DeleteMapping
    public void deleteMedicalRecord(@RequestParam String firstName, @RequestParam String lastName) {
        logger.info("DELETE /medicalRecord?firstName={}&lastName={}", firstName, lastName);
        boolean removed = medicalRecordService.deleteMedicalRecord(firstName, lastName);
        if (!removed) {
            logger.warn("No medical record found for {} {}. No deletion performed.", firstName, lastName);
        }
    }
}
