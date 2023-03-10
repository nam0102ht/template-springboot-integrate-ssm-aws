package com.ntnn.config;

import com.amazonaws.regions.Regions;
import com.amazonaws.services.simplesystemsmanagement.AWSSimpleSystemsManagement;
import com.amazonaws.services.simplesystemsmanagement.AWSSimpleSystemsManagementClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ParameterStoreConfigurations {

    @Value("${infrastructure.services.aws.region}")
    private String region;

    @Bean
    public AWSSimpleSystemsManagement getParameterStore() {
        AWSSimpleSystemsManagementClientBuilder awsSimpleSystemsManagementClientBuilder = AWSSimpleSystemsManagementClientBuilder.standard();
        awsSimpleSystemsManagementClientBuilder.withRegion(Regions.fromName(region));
        return awsSimpleSystemsManagementClientBuilder.build();
    }
}
