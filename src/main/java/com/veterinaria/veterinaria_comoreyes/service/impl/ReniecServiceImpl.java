package com.veterinaria.veterinaria_comoreyes.service.impl;

import com.veterinaria.veterinaria_comoreyes.dto.ReniecResponseDTO;
import com.veterinaria.veterinaria_comoreyes.service.IReniecService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class ReniecServiceImpl implements IReniecService {

    private final RestTemplate reniecRestTemplate;

    @Value("${reniec.api.url}")
    private String reniecApiUrl;

    public ReniecServiceImpl(@Qualifier("reniecRestTemplate") RestTemplate reniecRestTemplate) {
        this.reniecRestTemplate = reniecRestTemplate;
    }

    @Override
    public ReniecResponseDTO consultDni(String dni) {
        String url = reniecApiUrl + "?numero=" + dni;

        ResponseEntity<ReniecResponseDTO> response = reniecRestTemplate.exchange(
                url,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<>() {}
        );

        ReniecResponseDTO body = response.getBody();
        if (body == null || body.getNombres() == null) {
            throw new RuntimeException("No se encontraron datos para el DNI: " + dni);
        }
        return body;
    }
}
