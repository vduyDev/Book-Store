package com.example.identityservice.config;

import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class KeycloakConfig {
    @Value("${keycloak.realm}")
    private  String realm;
    @Value("${keycloak.adminClientId}")
    private String client;
    @Value("${keycloak.urls.auth}")
    private String url;
    @Value("${keycloak.adminClientSecret}")
    private String clientSecret;
    @Value("${keycloak.username}")
    private String username;
    @Value("${keycloak.password}")
    private String password;

    @Bean
    public Keycloak keycloak(){
        return KeycloakBuilder.builder()
                .clientId(client)
                .realm(realm)
                .grantType(OAuth2Constants.CLIENT_CREDENTIALS)
                .serverUrl(url)
                .username(username)
                .password(password)
                .clientSecret(clientSecret)
                .build();
    }
}
