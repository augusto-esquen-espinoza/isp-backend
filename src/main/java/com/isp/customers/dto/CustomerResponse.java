package com.isp.customers.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Customer representation returned by the API.
 */
public record CustomerResponse(
        Long id,
        String nombre,
        String apellido,
        String email,
        String dni,
        LocalDateTime fechaCreacion,
        LocalDate fechaNacimiento) {}
