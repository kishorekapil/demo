package com.isaclient.aws.sqs.config;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.sqs.AmazonSQSAsync;
import com.amazonaws.services.sqs.AmazonSQSAsyncClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.aws.messaging.core.QueueMessagingTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
public class AwsSqsConfig {

    @Value("${cloud.aws.region.static}")
    private String region;
    @Value("${cloud.aws.credentials.access-key}")
    private String awsAccessKey;
    @Value("${cloud.aws.credentials.secret-key}")
    private String awsSecretKey;
    @Value("${cloud.aws.sqs.end-point}")
    private String sqsEndPointURL;

    @Bean
    public QueueMessagingTemplate queueMessagingTemplate() {
        return new QueueMessagingTemplate(amazonSQSAsync());
    }


    @Primary
    @Bean
    public AmazonSQSAsync amazonSQSAsync() {
        return AmazonSQSAsyncClientBuilder.standard().withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration(sqsEndPointURL, region))
                .withCredentials(new AWSStaticCredentialsProvider(new BasicAWSCredentials(awsAccessKey, awsSecretKey)))
                .build();
    }
}
