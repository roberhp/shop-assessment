package com.liverpool.appsales.exam.customer.application.exception;

public class CustomerNotFoundException extends RuntimeException {

    public CustomerNotFoundException(String userId) {
        super("Customer not found: " + userId);
    }
}