package com.example.demo.aws;

public enum AWSBucket {

    BUCKET_NAME("ntloc-course-image");

    private final String bucketName;

    AWSBucket(String bucketName) {
        this.bucketName = bucketName;
    }

    public String getBucketName() {
        return bucketName;
    }
}
