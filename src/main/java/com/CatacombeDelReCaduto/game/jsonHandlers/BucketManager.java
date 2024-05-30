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

//Gestiore la connessione con client amazon, metodo per credenziali e internet
public class BucketManager implements AutoCloseable {
    private static final Logger logger = LoggerFactory.getLogger(BucketManager.class);

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

    public void uploadFile(String key, String uploadFilePath) {
        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(key)
                .build();
        s3Client.putObject(putObjectRequest, Paths.get(uploadFilePath));
        logger.info(uploadFilePath + " uploaded");
    }

    // scarica il file da aws e lo sovrascrive se esistente
    public void downloadFile(String key, String downloadFilePath) throws NoSuchKeyException {
        File file = new File(downloadFilePath);
        File bckFile = new File(downloadFilePath + ".backup");

        // se il file esiste lo rinomina temporaneamente
        if (file.exists())
            if (!file.renameTo(bckFile))
                throw new RuntimeException("impossible to rename the file");

        try{
            GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                    .bucket(bucketName)
                    .key(key)
                    .build();
            s3Client.getObject(getObjectRequest, Paths.get(downloadFilePath));
            logger.info(downloadFilePath + " downloaded");
        } catch (Exception e){
            // ripristina file se stato creato
            if (bckFile.exists() && !bckFile.renameTo(file))
                logger.error("impossible to delete the file");
            throw e;
        }

        // elimina file di bck se è stato creato
        if (bckFile.exists() && !bckFile.delete())
            throw new RuntimeException("impossible to delete the file");
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


    @Override
    public void close() {
        s3Client.close();
    }

    public static BucketManager loadConnection() throws IOException {
        AwsConnectionSettings.load();

        // inizializza connessione con il bucket
        return new BucketManager(AwsConnectionSettings.getAccessKey()
                , AwsConnectionSettings.getSecretKey(), AwsConnectionSettings.getRegion(), AwsConnectionSettings.getBucketName());
    }

    public static BucketManager loadExistConnection() throws IllegalArgumentException {
        // i file non sono stati sincronizzati non è possibile utilizzare la connessione esistente
        if (!AwsConnectionSettings.isloaded()){
            throw new IllegalArgumentException("no existing connection");
        }

        // inizializza connessione con il bucket
        return new BucketManager(AwsConnectionSettings.getAccessKey()
                , AwsConnectionSettings.getSecretKey(), AwsConnectionSettings.getRegion(), AwsConnectionSettings.getBucketName());
    }
}

