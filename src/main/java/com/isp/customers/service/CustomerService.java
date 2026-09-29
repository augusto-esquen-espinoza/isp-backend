package com.isp.customers.service;

import com.isp.customers.dto.CreateCustomerRequest;
import com.isp.customers.dto.CustomerResponse;
import java.util.List;

/**
 * Business operations for customers.
 */
public interface CustomerService {

    /**
     * Registers a new customer.
     *
     * @throws com.isp.customers.exception.DuplicateCustomerException if the dni or
     *     email already exists
     */
    CustomerResponse create(CreateCustomerRequest request);

    /**
     * Retrieves customers, optionally filtered by dni and/or email.
     *
     * @param dni optional dni filter
     * @param email optional email filter
     * @return matching customers, or all customers when no filter is provided
     */
    List<CustomerResponse> findAll(String dni, String email);
}
