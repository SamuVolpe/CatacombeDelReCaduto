package com.CatacombeDelReCaduto.game.jsonHandlers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import software.amazon.awssdk.regions.Region;

import java.io.File;
import java.io.IOException;

/**
 * Classe di utilita` per la lettura dei dati di connessione ad AWS
 */
public class AwsConnectionSettings {
    public static final String CONNECTION_FILE_PATH = "awsConnectionSettings.json";

    // serve per verificare se e' gia` stata caricata una configurazione valida
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
        setLoaded(false);
        JsonNode rootNode = null;
        // Carica il file dalle risorse
        File file = new File(CONNECTION_FILE_PATH);
        ObjectMapper objectMapper = new ObjectMapper();
        rootNode = objectMapper.readTree(file);
        // carica dati da JsonNode
        load(rootNode);
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

    /**
     * Segnala che la configurazione caricata è valida
     * @param loaded true se valida
     */
    public static void setLoaded(boolean loaded) { AwsConnectionSettings.loaded = loaded; }

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
