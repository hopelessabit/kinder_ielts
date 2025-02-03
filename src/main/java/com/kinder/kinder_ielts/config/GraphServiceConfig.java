package com.kinder.kinder_ielts.config;

import com.azure.core.credential.TokenCredential;
import com.azure.identity.ClientSecretCredentialBuilder;
import com.microsoft.graph.serviceclient.GraphServiceClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class GraphServiceConfig {

    @Value("${azure.tenantId}")
    private String tenantId;

    @Value("${azure.clientId}")
    private String clientId;

    @Value("${azure.clientSecret}")
    private String clientSecret;

    @Bean
    public GraphServiceClient graphServiceClient() {
        TokenCredential tokenCredential = new ClientSecretCredentialBuilder()
                .tenantId(tenantId)
                .clientId(clientId)
                .clientSecret(clientSecret)
                .build();

        return new GraphServiceClient(tokenCredential);
    }
}
