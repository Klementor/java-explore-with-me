package ru.practicum.ewm.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.practicum.ewm.hit.client.EndpointHitClient;

@Configuration
public class EndpointHitClientConfig {
    private final String serverUrl;

    public EndpointHitClientConfig(@Value("${stats-server.url}") String serverUrl) {
        this.serverUrl = serverUrl;
    }

    @Bean
    public EndpointHitClient createEndpointHitClient() {
        return new EndpointHitClient(serverUrl);
    }
}
