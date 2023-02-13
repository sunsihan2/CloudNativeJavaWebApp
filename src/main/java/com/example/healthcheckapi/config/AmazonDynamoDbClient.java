package com.example.healthcheckapi.config;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
public class AmazonDynamoDbClient {

    @Value("${aws.s3.region}")
    private String awsRegion;

    @Primary
    @Bean
    public AmazonDynamoDB generateDynamodbClient() {

        return AmazonDynamoDBClientBuilder
                .standard()
                .withRegion(awsRegion)
                .build();
    }
}
