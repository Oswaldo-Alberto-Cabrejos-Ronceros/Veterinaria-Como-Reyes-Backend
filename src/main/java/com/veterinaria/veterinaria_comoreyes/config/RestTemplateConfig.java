package com.veterinaria.veterinaria_comoreyes.config;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class RestTemplateConfig {

    @Value("${reniec.api.token}")
    private String reniecApiToken;

    @Bean(name = "reniecRestTemplate")
    public RestTemplate reniecRestTemplate() {
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.getInterceptors().add((request, body, execution) -> {
            request.getHeaders().add("Authorization", "Bearer " + reniecApiToken);
            request.getHeaders().add("Referer", "https://apis.net.pe/consulta-dni-api");
            return execution.execute(request, body);
        });
        return restTemplate;
    }
}
