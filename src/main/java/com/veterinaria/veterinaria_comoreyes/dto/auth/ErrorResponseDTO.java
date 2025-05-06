package com.veterinaria.veterinaria_comoreyes.dto.auth;

import java.time.Instant;

public record ErrorResponseDTO(
        String code,
        String message,
        Instant timestamp,
        String path
)
{ }
