package com.isp.customers.exception;

/**
 * Thrown when a customer is created with a DNI or email that already exists.
 */
public class DuplicateCustomerException extends RuntimeException {

    public DuplicateCustomerException(String message) {
        super(message);
    }
}
