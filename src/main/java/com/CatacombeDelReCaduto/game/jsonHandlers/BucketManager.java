package com.CatacombeDelReCaduto.game.jsonHandlers;

import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.ProfileCredentialsProvider;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;

import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.FileTime;
import java.time.Instant;

//Gestiore la connessione con client amazon, metodo per credenziali e internet
public class BucketManager implements AutoCloseable {

    private final String accessKey = "AKIA5FTY7DFKLLCRISU7";
    private final String secretKey = "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";
    private final Region region = Region.EU_CENTRAL_1;

    AwsBasicCredentials awsCreds = AwsBasicCredentials.create(accessKey, secretKey);

    private final S3Client s3Client = S3Client.builder()
            .region(region)
            .credentialsProvider(StaticCredentialsProvider.create(awsCreds))
            .build();

    private final String bucketName = "catacombe-del-re-caduto";

    public BucketManager() {

    }

    public boolean tryConnection(){
        try {
            HeadBucketRequest headBucketRequest = HeadBucketRequest.builder()
                    .bucket(bucketName)
                    .build();
            s3Client.headBucket(headBucketRequest);
            //System.out.println("Connessione con il bucket S3 riuscita. Il bucket esiste.");
            return true;
        } catch (S3Exception e) {
            //System.err.println("Connessione con il bucket S3 fallita. Motivo: " + e.awsErrorDetails().errorMessage());
            return false;
        }
    }

    public boolean getLastModified(String key){
        if(tryConnection()){
            // Ottieni la data di ultima modifica del file nel bucket S3
            HeadObjectRequest headObjectRequest = HeadObjectRequest.builder()
                    .bucket(bucketName)
                    .key(key) //key sarebbe il file all'interno
                    .build();
            HeadObjectResponse headObjectResponse = s3Client.headObject(headObjectRequest);
            Instant s3LastModified = headObjectResponse.lastModified();

            // Ottieni la data di ultima modifica del file locale
            Path filePath = Paths.get("C:\\Users\\UTENTE\\Desktop\\CatacombeDelReCaduto\\data\\player"+key);
            FileTime localFileTime = null;
            try {
                localFileTime = Files.getLastModifiedTime(filePath);
            } catch (IOException e) {
                System.err.println("Il file è inesistente o è stato danneggiato");
                return true;
            }
            Instant localLastModified = localFileTime.toInstant();

            // Confronta le due date
            if (s3LastModified.isAfter(localLastModified)) {
                System.out.println("Il file nel bucket S3 è più recente.");
                return true;
            } else if (localLastModified.isAfter(s3LastModified)) {
                System.out.println("Il file locale è più recente.");
                return false;
            } else {
                System.out.println("Entrambi i file hanno la stessa data di ultima modifica.");
                return true;
            }
        }
        return false;
    }

    public void uploadFile(String key, File file) {
        if(tryConnection()){
            PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                    .bucket(bucketName)
                    .key(key)
                    .build();
            s3Client.putObject(putObjectRequest, RequestBody.fromFile(file));
        }
    }

    //Controlla che esista il file nel downlaod
    public void downloadFile(String key, String downloadFilePath) {
        if(tryConnection()){
            GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                    .bucket(bucketName)
                    .key(key)
                    .build();
            s3Client.getObject(getObjectRequest, Paths.get(downloadFilePath));
        }
    }

    //metodo per eliminare il file
    public void deleteFile(String key, String deleteFilePath) {
        if(tryConnection()) {
            DeleteObjectRequest deleteObjectRequest = DeleteObjectRequest.builder()
                    .bucket(bucketName)
                    .key(key)
                    .build();
            s3Client.deleteObject(deleteObjectRequest);
        }
    }

    @Override
    public void close() {
        s3Client.close();
    }
}

