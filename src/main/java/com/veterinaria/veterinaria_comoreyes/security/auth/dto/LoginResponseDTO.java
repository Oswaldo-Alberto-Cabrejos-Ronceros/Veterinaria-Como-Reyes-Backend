package com.veterinaria.veterinaria_comoreyes.security.auth.dto;

import java.util.List;
import java.util.Map;
import java.util.Set;

public record LoginResponseDTO(
        Long userId,
        Long entityId,
        String mainRole, //(CLIENTE, VETERINARIO, RECEPCIONISTA, GERENTE GENERAL, ENCARGADO DE SEDE)
        Map<String, List<String>> GroupedPermissions
){ }
