package com.safetynetalerts.service;

import com.safetynetalerts.model.MedicalRecord;
import com.safetynetalerts.repository.DataRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MedicalRecordService {

    private static final Logger logger = LoggerFactory.getLogger(MedicalRecordService.class);

    private final DataRepository dataRepository;

    public MedicalRecordService(DataRepository dataRepository) {
        this.dataRepository = dataRepository;
    }

    /**
     * (Optionnel) Retourne la liste complète des MedicalRecords
     */
    public List<MedicalRecord> getAllMedicalRecords() {
        return dataRepository.getMedicalRecords();
    }

    /**
     * (Optionnel) Retrouve un MedicalRecord par (firstName, lastName).
     * Renvoie null si non trouvé.
     */
    public MedicalRecord getMedicalRecordByName(String firstName, String lastName) {
        return dataRepository.getMedicalRecords().stream()
                .filter(m -> m.getFirstName().equalsIgnoreCase(firstName)
                        && m.getLastName().equalsIgnoreCase(lastName))
                .findFirst()
                .orElse(null);
    }

    /**
     * Ajoute un nouveau MedicalRecord
     * S'il existe déjà (même firstName + lastName), on renvoie null ou on lève une
     * exception.
     */
    public MedicalRecord addMedicalRecord(MedicalRecord newRecord) {
        boolean exists = dataRepository.getMedicalRecords().stream()
                .anyMatch(m -> m.getFirstName().equalsIgnoreCase(newRecord.getFirstName())
                        && m.getLastName().equalsIgnoreCase(newRecord.getLastName()));
        if (exists) {
            logger.warn("MedicalRecord for {} {} already exists. No creation performed.",
                    newRecord.getFirstName(), newRecord.getLastName());
            return null; // ou lever une exception
        }

        dataRepository.getMedicalRecords().add(newRecord);
        logger.info("Added new medical record for {} {}. Birthdate={}, medications={}, allergies={}",
                newRecord.getFirstName(), newRecord.getLastName(),
                newRecord.getBirthdate(), newRecord.getMedications(), newRecord.getAllergies());
        return newRecord;
    }

    /**
     * Met à jour un MedicalRecord existant.
     * On identifie l'entrée par (firstName, lastName) et on met à jour
     * la birthdate, la liste de medications et la liste d'allergies.
     */
    public MedicalRecord updateMedicalRecord(MedicalRecord updatedRecord) {
        for (MedicalRecord mr : dataRepository.getMedicalRecords()) {
            if (mr.getFirstName().equalsIgnoreCase(updatedRecord.getFirstName())
                    && mr.getLastName().equalsIgnoreCase(updatedRecord.getLastName())) {

                logger.info("Updating medical record for {} {}",
                        mr.getFirstName(), mr.getLastName());
                mr.setBirthdate(updatedRecord.getBirthdate());
                mr.setMedications(updatedRecord.getMedications());
                mr.setAllergies(updatedRecord.getAllergies());
                return mr;
            }
        }
        logger.warn("MedicalRecord for {} {} not found. Update not performed.",
                updatedRecord.getFirstName(), updatedRecord.getLastName());
        return null;
    }

    /**
     * Supprime un MedicalRecord par (firstName, lastName).
     * Renvoie true si la suppression a eu lieu, false sinon.
     */
    public boolean deleteMedicalRecord(String firstName, String lastName) {
        List<MedicalRecord> medicalRecords = dataRepository.getMedicalRecords();
        boolean removed = medicalRecords.removeIf(m -> m.getFirstName().equalsIgnoreCase(firstName)
                && m.getLastName().equalsIgnoreCase(lastName));
        if (removed) {
            logger.info("Deleted medical record for {} {}", firstName, lastName);
        } else {
            logger.warn("No medical record found for {} {}. No deletion performed.", firstName, lastName);
        }
        return removed;
    }
}
