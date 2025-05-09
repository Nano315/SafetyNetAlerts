package com.safetynetalerts.controller;

import com.safetynetalerts.model.MedicalRecord;
import com.safetynetalerts.service.MedicalRecordService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Contrôleur gérant les opérations CRUD sur les dossiers médicaux
 * ainsi que deux endpoints « optionnels » pratiques pour le
 * débogage / l’administration.
 */
@RestController
@RequestMapping("/medicalRecord")
public class MedicalRecordController {

    /** Journalisation applicative. */
    private static final Logger LOG = LoggerFactory.getLogger(MedicalRecordController.class);

    private final MedicalRecordService medicalRecordService;

    public MedicalRecordController(MedicalRecordService medicalRecordService) {
        this.medicalRecordService = medicalRecordService;
    }

    /**
     * GET /medicalRecord : retourne la totalité
     * des dossiers médicaux chargés en mémoire.
     */
    @GetMapping
    public List<MedicalRecord> getAllMedicalRecords() {
        LOG.info("GET /medicalRecord – récupération de tous les dossiers");
        List<MedicalRecord> records = medicalRecordService.getAllMedicalRecords();
        LOG.info("{} dossier(s) médical(aux) trouvé(s)", records.size());
        return records;
    }

    /**
     * GET /medicalRecord/search : récupère un
     * dossier précis à partir du couple <code>firstName/lastName</code>.
     *
     * @param firstName prénom
     * @param lastName  nom
     */
    @GetMapping("/search")
    public MedicalRecord getMedicalRecordByName(@RequestParam String firstName,
            @RequestParam String lastName) {
        LOG.info("GET /medicalRecord/search – {} {}", firstName, lastName);
        MedicalRecord record = medicalRecordService.getMedicalRecordByName(firstName, lastName);
        if (record == null) {
            LOG.warn("Aucun dossier médical pour {} {}", firstName, lastName);
        }
        return record;
    }

    /**
     * POST /medicalRecord : ajoute un nouveau dossier.
     *
     * @param newRecord JSON représentant le dossier à créer
     * @return le dossier créé ou {@code null} s’il existe déjà
     */
    @PostMapping
    public MedicalRecord addMedicalRecord(@RequestBody MedicalRecord newRecord) {
        LOG.info("POST /medicalRecord – création dossier pour {} {}",
                newRecord.getFirstName(), newRecord.getLastName());
        return medicalRecordService.addMedicalRecord(newRecord);
    }

    /**
     * PUT /medicalRecord : met à jour un dossier existant.
     *
     * @param updatedRecord JSON contenant les nouvelles valeurs
     * @return le dossier mis à jour ou {@code null} si introuvable
     */
    @PutMapping
    public MedicalRecord updateMedicalRecord(@RequestBody MedicalRecord updatedRecord) {
        LOG.info("PUT /medicalRecord – mise à jour dossier pour {} {}",
                updatedRecord.getFirstName(), updatedRecord.getLastName());
        return medicalRecordService.updateMedicalRecord(updatedRecord);
    }

    /**
     * DELETE /medicalRecord : supprime un dossier.
     *
     * @param firstName prénom
     * @param lastName  nom
     */
    @DeleteMapping
    public void deleteMedicalRecord(@RequestParam String firstName,
            @RequestParam String lastName) {
        LOG.info("DELETE /medicalRecord – {} {}", firstName, lastName);
        boolean removed = medicalRecordService.deleteMedicalRecord(firstName, lastName);
        if (!removed) {
            LOG.warn("Suppression impossible : aucun dossier pour {} {}", firstName, lastName);
        }
    }
}
