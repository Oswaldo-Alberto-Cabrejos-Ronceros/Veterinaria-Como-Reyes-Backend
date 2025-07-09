package com.veterinaria.veterinaria_comoreyes.dto.Payment;

public record PaymentTicketDTO(
                String numeroBoleta,
                String fechaEmision,
                String clienteNombre,
                String clienteDni,
                String mascotaNombre,
                String mascotaEspecie,
                String mascotaRaza,
                String servicioNombre,
                double servicioPrecio,
                double subtotal,
                double igv,
                double total,
                String totalEnLetras,
                String metodoPago,
                String veterinario,
                String sedeDireccion,
                String sedeTelefono,
                String ruc) {
}