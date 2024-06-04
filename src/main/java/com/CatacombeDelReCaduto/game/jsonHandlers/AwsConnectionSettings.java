package com.CatacombeDelReCaduto.game.jsonHandlers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import software.amazon.awssdk.regions.Region;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

/**
 * Classe di utilita` per la lettura dei dati di connessione ad AWS
 */
public class AwsConnectionSettings {
    public static final String CONNECTION_FILE_PATH = "awsConnectionSettings.json";

    // serve per verificare se e' gia` stata caricata una configurazione
    private static boolean loaded = false;

    // dati connessione
    private static String accessKey = null;
    private static String secretKey = null;
    private static Region region = null;
    private static String bucketName = null;

    /**
     * Carica dati dal file di default
     * @throws IOException eccezione se la lettura non va a buon fine
     */
    public static void load() throws IOException {
        loaded = false;
        JsonNode rootNode = null;
        // Carica il file dalle risorse
        try (InputStream inputStream = AwsConnectionSettings.class.getClassLoader().getResourceAsStream(CONNECTION_FILE_PATH)) {
            if (inputStream == null) {
                throw new IOException("File not found in resources: " + CONNECTION_FILE_PATH);
            }
            ObjectMapper objectMapper = new ObjectMapper();
            rootNode = objectMapper.readTree(inputStream);
        }
        // carica dati da JsonNode
        load(rootNode);
        loaded = true;
    }

    /**
     * Carica dati dal file dato
     * @param rootNode nodo json da cui caricare i dati
     */
    private static void load(JsonNode rootNode) {
        accessKey = rootNode.get("accessKey").asText();
        secretKey = rootNode.get("secretKey").asText();
        region = Region.of(rootNode.get("region").asText());
        bucketName = rootNode.get("bucketName").asText();
    }

    /**
     * Verifica se e` gia` stata caricata una configurazione
     * @return true se e` stata caricata, false altrimenti
     */
    public static boolean isloaded(){
        return loaded;
    }

    public static String getAccessKey() {
        return accessKey;
    }

    public static String getSecretKey() {
        return secretKey;
    }

    public static Region getRegion() {
        return region;
    }

    public static String getBucketName() {
        return bucketName;
    }
}
