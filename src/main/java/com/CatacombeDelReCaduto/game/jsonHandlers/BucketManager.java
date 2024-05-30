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
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.attribute.FileTime;
import java.util.Map;

//Gestiore la connessione con client amazon, metodo per credenziali e internet
public class BucketManager implements AutoCloseable {
    private static final Logger logger = LoggerFactory.getLogger(BucketManager.class);
    // indica se i file sono stati sincronizzati dal cloud a inizio gioco
    public static boolean filesSync = false;

    private final S3Client s3Client;
    private final String bucketName;

    public BucketManager(String accessKey, String secretKey, Region region, String bucketName) {
        this.bucketName = bucketName;

        // init amazon credentials
        AwsBasicCredentials awsCreds = AwsBasicCredentials.create(accessKey, secretKey);

        // init s3 client
        s3Client = S3Client.builder()
                .region(region)
                .credentialsProvider(StaticCredentialsProvider.create(awsCreds))
                .build();
    }

    public boolean tryConnection(){
        try {
            HeadBucketRequest headBucketRequest = HeadBucketRequest.builder()
                    .bucket(bucketName)
                    .build();
            s3Client.headBucket(headBucketRequest);
            logger.info("Connessione con il bucket S3 riuscita. Il bucket esiste.");
            return true;
        } catch (S3Exception e) {
            logger.error("Connessione con il bucket S3 fallita", e);
            return false;
        }
    }

    public void uploadFile(String key, String uploadFilePath) {
        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(key)
                .build();
        s3Client.putObject(putObjectRequest, Paths.get(uploadFilePath));
        logger.info(uploadFilePath + " uploaded");
    }

    public boolean uploadIfNewer(String key, String localFilePath) {
        try {
            // Ottieni il timestamp dell'ultimo modificato del file locale
            File localFile = new File(localFilePath);
            if (!localFile.exists()) {
                logger.info("Il file locale non esiste.");
                return false;
            }
            FileTime localLastModified = (FileTime) Files.getAttribute(Paths.get(localFilePath), "lastModifiedTime");

            boolean shouldUpload = false;

            try {
                // Ottieni i metadati del file nel bucket S3
                HeadObjectRequest headObjectRequest = HeadObjectRequest.builder()
                        .bucket(bucketName)
                        .key(key)
                        .build();
                HeadObjectResponse headObjectResponse = s3Client.headObject(headObjectRequest);
                FileTime s3LastModified = FileTime.from(headObjectResponse.lastModified());

                // Confronta i timestamp
                if (localLastModified.compareTo(s3LastModified) > 0) {
                    shouldUpload = true;
                }
            } catch (NoSuchKeyException e) {
                // Se il file non esiste nel bucket, carica il file locale
                shouldUpload = true;
            }

            // Se il file locale è più recente o il file non esiste nel bucket, caricalo
            if (shouldUpload) {
                uploadFile(key, localFile.getPath());
                return true;
            } else {
                logger.info("Il file nel bucket è già aggiornato.");
            }
        }catch (IOException e){
            throw new RuntimeException(e);
        }

        return false;
    }

    // scarica il file da aws e lo sovrascrive se esistente
    public void downloadFile(String key, String downloadFilePath) {
        File file = new File(downloadFilePath);
        File bckFile = new File(downloadFilePath + ".backup");

        try {
            // se il file esiste lo rinomina temporaneamente
            if (file.exists())
                if (!file.renameTo(bckFile))
                    throw new RuntimeException("impossible to rename the file");

        }catch (Exception e){
            logger.error("", e);
            return;
        }

        try{
            GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                    .bucket(bucketName)
                    .key(key)
                    .build();
            s3Client.getObject(getObjectRequest, Paths.get(downloadFilePath));
            logger.info(downloadFilePath + " downloaded");

            // elimina file di bck se è stato creato
            if (bckFile.exists() && !bckFile.delete())
                throw new RuntimeException("impossible to delete the file");
        }catch (Exception e){
            // ripristina file se stato creato
            if (bckFile.exists() && !bckFile.renameTo(file))
                throw new RuntimeException("impossible to rename the file");
        }
    }

    public boolean downloadIfNewer(String key, String downloadFilePath) {
        try {
            // Ottieni i metadati del file nel bucket S3
            HeadObjectRequest headObjectRequest = HeadObjectRequest.builder()
                    .bucket(bucketName)
                    .key(key)
                    .build();
            HeadObjectResponse headObjectResponse = s3Client.headObject(headObjectRequest);
            FileTime s3LastModified = FileTime.from(headObjectResponse.lastModified());

            // Ottieni il timestamp dell'ultimo modificato del file locale
            File localFile = new File(downloadFilePath);
            if (localFile.exists()) {
                FileTime localLastModified = (FileTime) Files.getAttribute(Paths.get(downloadFilePath), "lastModifiedTime");

                // Confronta i timestamp
                if (s3LastModified.compareTo(localLastModified) > 0) {
                    // Se il file S3 è più recente, scarica e sovrascrivi
                    downloadFile(key, downloadFilePath);
                    return true;
                }
            } else {
                // Se il file locale non esiste, scaricalo
                downloadFile(key, downloadFilePath);
                return true;
            }

        } catch (NoSuchKeyException e) {
            logger.info("Il file non esiste nel bucket.");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return false;
    }

    //metodo per eliminare il file
    public void deleteFile(String key, String deleteFilePath) {
        DeleteObjectRequest deleteObjectRequest = DeleteObjectRequest.builder()
                .bucket(bucketName)
                .key(key)
                .build();

        s3Client.deleteObject(deleteObjectRequest);
        logger.info(deleteFilePath + " deleted");
    }

    public boolean syncPlayerFiles(){
        // scarica file salvataggi
        boolean downloaded = downloadIfNewer(FilesManager.SAVES_FILE_NAME, FilesManager.SAVES_FILE_PATH);
        // carica eventuale file salvataggi
        if (!downloaded)
            uploadIfNewer(FilesManager.SAVES_FILE_NAME, FilesManager.SAVES_FILE_PATH);

        // fa sync per ogni singolo file di salvataggio
        Map<Long, String> games = FilesManager.loadGames();
        for (var game : games.entrySet()){
            // scarica file salvataggio
            downloaded = downloadIfNewer(FilesManager.gameFileName(game.getKey(), game.getValue())
                    , FilesManager.PLAYER_ROOT + "\\" + FilesManager.gameFileName(game.getKey(), game.getValue()));
            // carica eventuale file salvataggio
            if (!downloaded)
                uploadIfNewer(FilesManager.gameFileName(game.getKey(), game.getValue())
                        , FilesManager.PLAYER_ROOT + "\\" + FilesManager.gameFileName(game.getKey(), game.getValue()));
        }

        filesSync = true;
        return filesSync;
    }

    public void syncFile(String key, String filePath) {
        // scarica file
        boolean downloaded = downloadIfNewer(key, filePath);
        // se non ha scaricato, carica
        if (!downloaded)
            uploadIfNewer(key, filePath);
    }


    @Override
    public void close() {
        s3Client.close();
    }

    public static BucketManager loadConnection() {
        filesSync = false;

        try {
            AwsConnectionSettings.load();
        } catch (IOException | IllegalArgumentException e){
            throw new RuntimeException(e);
        }

        // inizializza connessione con il bucket
        return new BucketManager(AwsConnectionSettings.getAccessKey()
                , AwsConnectionSettings.getSecretKey(), AwsConnectionSettings.getRegion(), AwsConnectionSettings.getBucketName());
    }

    public static BucketManager loadExistConnection() {
        // i file non sono stati sincronizzati non è possibile utilizzare la connessione esistente
        if (!filesSync){
            throw new IllegalArgumentException("no existing connection");
        }

        // inizializza connessione con il bucket
        return new BucketManager(AwsConnectionSettings.getAccessKey()
                , AwsConnectionSettings.getSecretKey(), AwsConnectionSettings.getRegion(), AwsConnectionSettings.getBucketName());
    }
}

