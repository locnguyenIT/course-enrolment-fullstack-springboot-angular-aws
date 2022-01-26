package com.example.demo.aws;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.*;
import com.amazonaws.util.IOUtils;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Optional;

@AllArgsConstructor
@Service
public class AWSFileStore {

    private final AmazonS3 s3;

    public void upload(String path, String filename, InputStream inputStream, Optional<Map<String,String>> optionalMetadata){
        ObjectMetadata metadata = new ObjectMetadata();
        optionalMetadata.ifPresent(map -> {
            if(!map.isEmpty()){
                map.forEach((key,value) -> metadata.addUserMetadata(key,value));
            }
        });
        try {
            s3.putObject(path,filename,inputStream,metadata);
        }catch (AmazonServiceException e){
            throw new IllegalStateException("Failed to store file to AWS S3",e);
        }
    }

    public void delete(String bucket, String key) {
        try {
            ObjectListing objectListing = s3.listObjects(bucket,key); //ObjectListing is list of key
            for (S3ObjectSummary objectSummary: objectListing.getObjectSummaries()) {
                s3.deleteObject(bucket,objectSummary.getKey());
            }
        }catch (AmazonServiceException e){
            throw new IllegalStateException("Failed to delete file on AWS S3",e);
        }

    }

    public void create(String bucket, String folder) {
        try {
            ObjectMetadata objectMetadata = new ObjectMetadata();
            objectMetadata.setContentLength(0);
            InputStream inputStream = new ByteArrayInputStream(new byte[0]);
            //PutObjectRequest putObjectRequest = new PutObjectRequest(bucket,folder+"/",inputStream,objectMetadata);
            s3.putObject(bucket,folder+"/",inputStream,objectMetadata);
        }catch (AmazonServiceException e){
            throw new IllegalStateException("Failed to create folder to AWS S3",e);
        }
    }

    public void coppy(String bucket, String sourceKey, String destinationKey ) {
        try {
            s3.copyObject(bucket,sourceKey,bucket,destinationKey);
        }catch (AmazonServiceException e){
            throw new IllegalStateException("Failed to copy folder",e);
        }
    }

    public byte[] download(String path, String key) {
        try{
            S3Object objects = s3.getObject(path,key);
            return IOUtils.toByteArray(objects.getObjectContent());
        }catch (AmazonServiceException | IOException e){
            throw new IllegalStateException("Failed to download from s3",e);
        }
    }
}
