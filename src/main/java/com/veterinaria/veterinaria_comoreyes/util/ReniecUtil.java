package com.veterinaria.veterinaria_comoreyes.util;

import com.veterinaria.veterinaria_comoreyes.dto.ReniecResponseDTO;
import com.veterinaria.veterinaria_comoreyes.exception.ReniecDataMismatchException;
import com.veterinaria.veterinaria_comoreyes.service.IReniecService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.Normalizer;

@Component
public class ReniecUtil {

    private final IReniecService reniecService;


    @Autowired
    public ReniecUtil(IReniecService reniecService) {
        this.reniecService = reniecService;
    }

    public void validateData (String dni, String name, String lastName) {
        ReniecResponseDTO dto = reniecService.consultDni(dni);

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
