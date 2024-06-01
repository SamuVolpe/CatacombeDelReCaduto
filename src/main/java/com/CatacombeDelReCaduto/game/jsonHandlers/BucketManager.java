package com.CatacombeDelReCaduto.game.jsonHandlers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;

/**
 * Gestisce la connessione con Amazon S3.
 */
public class BucketManager implements AutoCloseable {
    private static final Logger logger = LoggerFactory.getLogger(BucketManager.class);

    private final S3Client s3Client;
    private final String bucketName;

    /**
     * Costruttore per inizializzare le credenziali AWS e il client S3.
     *
     * @param accessKey La chiave di accesso AWS.
     * @param secretKey La chiave segreta AWS.
     * @param region La regione AWS.
     * @param bucketName Il nome del bucket S3.
     */
    public BucketManager(String accessKey, String secretKey, Region region, String bucketName) {
        this.bucketName = bucketName;

        // Inizializza le credenziali AWS
        AwsBasicCredentials awsCreds = AwsBasicCredentials.create(accessKey, secretKey);

        // Inizializza il client S3
        s3Client = S3Client.builder()
                .region(region)
                .credentialsProvider(StaticCredentialsProvider.create(awsCreds))
                .build();
    }

    /**
     * Carica un file su S3.
     *
     * @param key La chiave (nome) con cui il file sarà memorizzato su S3.
     * @param uploadFilePath Il percorso del file da caricare.
     */
    public void uploadFile(String key, String uploadFilePath) {
        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(key)
                .build();
        s3Client.putObject(putObjectRequest, Paths.get(uploadFilePath));
        logger.info(uploadFilePath + " uploaded");
    }

    /**
     * Scarica un file da S3 e lo salva localmente. Se esiste già un file con lo stesso nome, viene sovrascritto.
     *
     * @param key La chiave (nome) del file su S3.
     * @param downloadFilePath Il percorso locale dove il file sarà scaricato.
     * @throws NoSuchKeyException Se il file non esiste su S3.
     */
    public void downloadFile(String key, String downloadFilePath) throws NoSuchKeyException {
        File file = new File(downloadFilePath);
        File bckFile = new File(downloadFilePath + ".backup");

        // Se il file esiste, lo rinomina temporaneamente
        if (file.exists())
            if (!file.renameTo(bckFile))
                throw new RuntimeException("impossible to rename the file");

        try {
            GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                    .bucket(bucketName)
                    .key(key)
                    .build();
            s3Client.getObject(getObjectRequest, Paths.get(downloadFilePath));
            logger.info(downloadFilePath + " downloaded");
        } catch (Exception e) {
            // Ripristina il file se è stato creato un backup
            if (bckFile.exists() && !bckFile.renameTo(file))
                logger.error("impossible to delete the file");
            throw e;
        }

        // Elimina il file di backup se è stato creato
        if (bckFile.exists() && !bckFile.delete())
            throw new RuntimeException("impossible to delete the file");
    }

    /**
     * Elimina un file da S3.
     *
     * @param key La chiave (nome) del file su S3.
     * @param deleteFilePath Il percorso locale del file da eliminare.
     */
    public void deleteFile(String key, String deleteFilePath) {
        DeleteObjectRequest deleteObjectRequest = DeleteObjectRequest.builder()
                .bucket(bucketName)
                .key(key)
                .build();

        s3Client.deleteObject(deleteObjectRequest);
        logger.info(deleteFilePath + " deleted");
    }

    /**
     * Chiude il client S3.
     */
    @Override
    public void close() {
        s3Client.close();
    }

    /**
     * Carica le impostazioni della connessione AWS e inizializza un BucketManager.
     *
     * @return Un'istanza di BucketManager inizializzata.
     * @throws IOException Se le impostazioni non possono essere caricate.
     */
    public static BucketManager loadConnection() throws IOException {
        // inizializza connessione da settings
        AwsConnectionSettings.load();

        return new BucketManager(AwsConnectionSettings.getAccessKey(),
                AwsConnectionSettings.getSecretKey(), AwsConnectionSettings.getRegion(), AwsConnectionSettings.getBucketName());
    }

    /**
     * Inizializza un BucketManager utilizzando una connessione esistente.
     *
     * @return Un'istanza di BucketManager inizializzata.
     * @throws IllegalArgumentException Se non e` stata caricata una connessione.
     */
    public static BucketManager loadExistConnection() throws IllegalArgumentException {
        // controlla se la connessione esiste
        if (!AwsConnectionSettings.isloaded()) {
            throw new IllegalArgumentException("no existing connection");
        }

        return new BucketManager(AwsConnectionSettings.getAccessKey(),
                AwsConnectionSettings.getSecretKey(), AwsConnectionSettings.getRegion(), AwsConnectionSettings.getBucketName());
    }
}