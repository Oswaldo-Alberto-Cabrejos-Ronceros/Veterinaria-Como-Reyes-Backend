package com.veterinaria.veterinaria_comoreyes.dto.auth;

import java.util.Set;

public record LoginResponseDTO(
        String token,
        Long userId,
        Long entityId,
        String type, // "E" (empleado) o "C" (cliente)
        Set<String> permissions
){ }
