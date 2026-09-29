package com.isp.customers.service;

import com.isp.customers.dto.CreateCustomerRequest;
import com.isp.customers.dto.CustomerResponse;
import com.isp.customers.entity.Customer;
import com.isp.customers.exception.DuplicateCustomerException;
import com.isp.customers.mapper.CustomerMapper;
import com.isp.customers.repository.CustomerRepository;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository repository;

    public CustomerServiceImpl(CustomerRepository repository) {
        this.repository = repository;
    }

    @Override
    @Transactional
    public CustomerResponse create(CreateCustomerRequest request) {
        if (repository.existsByDni(request.dni())) {
            throw new DuplicateCustomerException("dni already registered: " + request.dni());
        }
        if (repository.existsByEmail(request.email())) {
            throw new DuplicateCustomerException("email already registered: " + request.email());
        }

        Customer saved = repository.save(CustomerMapper.toEntity(request));
        return CustomerMapper.toResponse(saved);
    }

    @Override
    public List<CustomerResponse> findAll(String dni, String email) {
        boolean hasDni = dni != null && !dni.isBlank();
        boolean hasEmail = email != null && !email.isBlank();

        List<Customer> customers;
        if (hasDni && hasEmail) {
            customers = repository.findByDniOrEmail(dni.trim(), email.trim());
        } else if (hasDni) {
            customers = repository.findByDni(dni.trim()).stream().toList();
        } else if (hasEmail) {
            customers = repository.findByEmail(email.trim()).stream().toList();
        } else {
            customers = repository.findAll(Sort.by(Sort.Direction.DESC, "fechaCreacion"));
        }

        return customers.stream().map(CustomerMapper::toResponse).toList();
    }
}
