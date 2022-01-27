package com.example.demo.aws;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailService;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailServiceClientBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AWSConfig {

    AWSCredentials awsCredentials = new BasicAWSCredentials("your-AccessKey",
            "your-SecretKey");
    @Bean
    public AmazonS3 s3()
    {
        return AmazonS3ClientBuilder
                .standard()
                .withRegion("Your-Region")
                .withCredentials(new AWSStaticCredentialsProvider(awsCredentials))
                .build();
    }

    @Bean
    public AmazonSimpleEmailService emailService () {
        return AmazonSimpleEmailServiceClientBuilder
                .standard()
                .withRegion("Your-Region")
                .withCredentials(new AWSStaticCredentialsProvider(awsCredentials))
                .build();
    }
}
