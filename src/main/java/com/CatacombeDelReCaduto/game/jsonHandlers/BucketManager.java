package com.CatacombeDelReCaduto.game.jsonHandlers;

import software.amazon.awssdk.auth.credentials.ProfileCredentialsProvider;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.File;
import java.nio.file.Paths;

public class BucketManager {
    private final S3Client s3Client = S3Client.builder()
            .region(Region.EU_CENTRAL_1)
            .credentialsProvider(ProfileCredentialsProvider.create())
            .build();
    private final String bucketName = "catacombe-del-re-caduto";

    public BucketManager() {

    }

    public void uploadFile(String key, File file) {
        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(key)
                .build();
        s3Client.putObject(putObjectRequest, RequestBody.fromFile(file));
    }

    public void downloadFile(String key, String downloadFilePath) {
        GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                .bucket(bucketName)
                .key(key)
                .build();
        s3Client.getObject(getObjectRequest, Paths.get(downloadFilePath));
    }

    public void close() {
        s3Client.close();
    }
}

