package com.veterinaria.veterinaria_comoreyes.security.auth.dto;

import java.util.List;
import java.util.Map;
import java.util.Set;

public record LoginResponseDTO(
        Long userId,
        Long entityId,
        String type, // "E" (empleado) o "C" (cliente)
        String mainRole,
        Map<String, List<String>> GroupedPermissions
){ }
