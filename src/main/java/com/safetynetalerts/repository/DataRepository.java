package com.safetynetalerts.repository;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.safetynetalerts.model.Firestation;
import com.safetynetalerts.model.MedicalRecord;
import com.safetynetalerts.model.Person;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Repository;

import jakarta.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@Repository
public class DataRepository {

    // Listes en mémoire (stockent toutes les données du JSON)
    private List<Person> persons = new ArrayList<>();
    private List<Firestation> firestations = new ArrayList<>();
    private List<MedicalRecord> medicalRecords = new ArrayList<>();

    // Chemin vers le fichier JSON (défini dans application.properties)
    // Exemple: data.json placé dans src/main/resources
    // et application.properties contient "data.file=classpath:data.json"
    @Value("${data.file:classpath:data.json}")
    private Resource dataFile;

    /**
     * Méthode appelée automatiquement au démarrage de l'application.
     * Elle charge le fichier JSON en mémoire.
     */
    @PostConstruct
    private void loadData() {
        ObjectMapper objectMapper = new ObjectMapper();
        try (InputStream is = dataFile.getInputStream()) {

            // 1) Lire tout le JSON
            JsonNode root = objectMapper.readTree(is);

            // 2) Extraire le tableau "persons"
            JsonNode personsNode = root.get("persons");
            persons = objectMapper.readValue(personsNode.traverse(), new TypeReference<>() {
            });

            // 3) Extraire le tableau "firestations"
            JsonNode firestationsNode = root.get("firestations");
            firestations = objectMapper.readValue(firestationsNode.traverse(), new TypeReference<>() {
            });

            // 4) Extraire le tableau "medicalrecords"
            JsonNode medicalRecordsNode = root.get("medicalrecords");
            medicalRecords = objectMapper.readValue(medicalRecordsNode.traverse(), new TypeReference<>() {
            });

            System.out.println("Data loaded successfully from JSON file.");

        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Error loading JSON file: " + e.getMessage());
        }
    }

    // --- Getters ---
    public List<Person> getPersons() {
        return persons;
    }

    public List<Firestation> getFirestations() {
        return firestations;
    }

    public List<MedicalRecord> getMedicalRecords() {
        return medicalRecords;
    }

    // --- Setters (optionnels) ---
    // Vous pouvez ajouter des setters si vous prévoyez de mettre à jour les données
    // en mémoire.
}
