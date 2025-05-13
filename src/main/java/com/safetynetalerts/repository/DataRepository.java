package com.safetynetalerts.repository;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.safetynetalerts.model.Firestation;
import com.safetynetalerts.model.MedicalRecord;
import com.safetynetalerts.model.Person;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.node.ObjectNode;

/**
 * Repository en mémoire : lit le fichier JSON au démarrage et expose
 * trois listes (persons, firestations, medicalRecords).
 */
@Repository
public class DataRepository {

    private static final Logger LOG = LoggerFactory.getLogger(DataRepository.class);

    /* ------------------------------------------------------------------ */
    /* Collections en mémoire */
    /* ------------------------------------------------------------------ */

    private List<Person> persons = new ArrayList<>();
    private List<Firestation> firestations = new ArrayList<>();
    private List<MedicalRecord> medicalRecords = new ArrayList<>();

    /* ------------------------------------------------------------------ */
    /* Chargement du fichier JSON */
    /* ------------------------------------------------------------------ */

    /** Chemin du fichier. */
    @Value("${data.file:classpath:data.json}")
    private Resource dataFile;

    /**
     * Chargement automatique au démarrage.
     * 
     * Toute la logique de parsing JSON est centralisée ici afin de pouvoir
     * manipuler les données intégralement en mémoire.
     */
    @PostConstruct
    private void loadData() {
        ObjectMapper mapper = new ObjectMapper();
        try (InputStream is = dataFile.getInputStream()) {

            JsonNode root = mapper.readTree(is);

            persons = mapper.readValue(root.get("persons").traverse(),
                    new TypeReference<>() {
                    });

            firestations = mapper.readValue(root.get("firestations").traverse(),
                    new TypeReference<>() {
                    });

            medicalRecords = mapper.readValue(root.get("medicalrecords").traverse(),
                    new TypeReference<>() {
                    });

            LOG.info("JSON chargé : {} persons, {} firestations, {} medicalRecords",
                    persons.size(), firestations.size(), medicalRecords.size());

        } catch (IOException e) {
            LOG.error("Erreur lors du chargement des données JSON", e);
        }
    }

    /* ------------------------------------------------------------------ */
    /* Écrit l’état actuel des listes dans le fichier JSON */
    /* ------------------------------------------------------------------ */

    public synchronized void saveData() {
        // On reconstruit un ObjectNode pour garder le même ordre de clés
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode root = mapper.createObjectNode();
        root.set("persons", mapper.valueToTree(persons));
        root.set("firestations", mapper.valueToTree(firestations));
        root.set("medicalrecords", mapper.valueToTree(medicalRecords));

        try {
            // ⚠️ dataFile est un Resource ; on obtient son Path pour écrire dessus
            Path path = Path.of(dataFile.getFile().toURI());
            ObjectWriter writer = mapper.writerWithDefaultPrettyPrinter();
            // overwrite le contenu
            Files.writeString(path, writer.writeValueAsString(root),
                    StandardOpenOption.TRUNCATE_EXISTING);
            LOG.debug("JSON persisté : {}", path.toAbsolutePath());
        } catch (IOException e) {
            LOG.error("Impossible d’écrire le fichier JSON", e);
        }
    }

    /* ------------------------------------------------------------------ */
    /* Accesseurs (lecture seule) */
    /* ------------------------------------------------------------------ */

    public List<Person> getPersons() {
        return persons;
    }

    public List<Firestation> getFirestations() {
        return firestations;
    }

    public List<MedicalRecord> getMedicalRecords() {
        return medicalRecords;
    }
}
