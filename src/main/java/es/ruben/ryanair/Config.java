package es.ruben.ryanair;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class Config {

    @Value("${ryanair.api}")
    private String ryanairApiUrl;

    @Bean
    public WebClient ryanairWebClient() {
        return WebClient.builder()
                .baseUrl(ryanairApiUrl)
                .build();
    }
}
