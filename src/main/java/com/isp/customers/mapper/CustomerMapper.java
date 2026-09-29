package com.isp.customers.mapper;

import com.isp.customers.dto.CreateCustomerRequest;
import com.isp.customers.dto.CustomerResponse;
import com.isp.customers.entity.Customer;

/**
 * Stateless mapper between {@link Customer} and its DTOs.
 */
public final class CustomerMapper {

    private CustomerMapper() {
        // utility class
    }

    public static Customer toEntity(CreateCustomerRequest request) {
        return Customer.builder()
                .nombre(request.nombre().trim())
                .apellido(request.apellido().trim())
                .email(request.email().trim().toLowerCase())
                .dni(request.dni().trim())
                .fechaNacimiento(request.fechaNacimiento())
                .build();
    }

    public static CustomerResponse toResponse(Customer customer) {
        return new CustomerResponse(
                customer.getId(),
                customer.getNombre(),
                customer.getApellido(),
                customer.getEmail(),
                customer.getDni(),
                customer.getFechaCreacion(),
                customer.getFechaNacimiento());
    }
}
