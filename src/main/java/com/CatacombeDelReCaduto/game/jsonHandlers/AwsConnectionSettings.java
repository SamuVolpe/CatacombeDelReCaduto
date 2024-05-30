package com.CatacombeDelReCaduto.game.jsonHandlers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import software.amazon.awssdk.regions.Region;

import java.io.File;
import java.io.IOException;

public class AwsConnectionSettings {
    public static final String DEFAULT_CONNECTION_FILE_PATH = FilesManager.GAME_ROOT + "\\" + "awsConnectionSettings.json";

    // is loaded
    private static boolean loaded = false;

    // dati connessione
    private static String accessKey = null;
    private static String secretKey = null;
    private static Region region = null;
    private static String bucketName = null;

    public static void load() throws IOException {
        load(DEFAULT_CONNECTION_FILE_PATH);
    }

    public static void load(String filePath) throws IOException{
        loaded = false;
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode rootNode = objectMapper.readTree(new File(filePath));

        accessKey = rootNode.get("accessKey").asText();
        secretKey = rootNode.get("secretKey").asText();
        region = Region.of(rootNode.get("region").asText());
        bucketName = rootNode.get("bucketName").asText();
        loaded = true;
    }

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
