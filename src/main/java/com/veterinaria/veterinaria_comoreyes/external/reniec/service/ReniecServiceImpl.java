package com.veterinaria.veterinaria_comoreyes.external.reniec.service;

import com.veterinaria.veterinaria_comoreyes.external.reniec.exception.ReniecDataMismatchException;
import com.veterinaria.veterinaria_comoreyes.external.reniec.dto.ReniecResponseDTO;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.text.Normalizer;

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

    @Override
    public void validateIdentityReniec(String dni, String name, String lastName) {
        ReniecResponseDTO dto = consultDni(dni);

        // 1. Limpiar y normalizar apellidos de RENIEC
        String reniecPaterno = clean(dto.getApellidoPaterno());
        String reniecMaterno = clean(dto.getApellidoMaterno());

        // 2. Partir el fullLastName del usuario
        String[] parts = lastName.trim().split("\\s+");
        if (parts.length < 2) {
            throw new ReniecDataMismatchException("Debe proporcionar dos apellidos");
        }
        String userPaterno = clean(parts[0]);
        String userMaterno = clean(parts[1]);

        // 3. Comparar
        if (!reniecPaterno.equals(userPaterno) || !reniecMaterno.equals(userMaterno)) {
            throw new ReniecDataMismatchException("Los datos no coinciden con los registrados en RENIEC");
        }
    }


    /**
     * Elimina tildes, pasa a mayúsculas y quita todo excepto A–Z.
     */
    private String clean(String input) {
        if (input == null) return "";

        // 1. Descomponer acentos
        String normalized = Normalizer.normalize(input, Normalizer.Form.NFD);
        // 2. Eliminar marcas diacríticas
        String withoutAccents = normalized.replaceAll("\\p{M}", "");
        // 3. Quitar TODO lo que no sea letra (A–Z, a–z) o espacio
        String onlyLetters = withoutAccents.replaceAll("[^\\p{L} ]", "");
        // 4. Pasar a MAYÚSCULAS y recortar
        return onlyLetters.toUpperCase().trim();
    }
}
