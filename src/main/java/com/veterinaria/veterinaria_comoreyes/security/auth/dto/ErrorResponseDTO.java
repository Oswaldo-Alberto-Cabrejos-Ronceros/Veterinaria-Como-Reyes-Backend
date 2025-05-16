package com.veterinaria.veterinaria_comoreyes.security.auth.dto;

import java.time.Instant;

public record ErrorResponseDTO(
        String code,
        String message,
        Instant timestamp,
        String path
)
{ }
