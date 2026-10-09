package com.liverpool.appsales.exam.customer.application.exception;

public class CustomerAlreadyExistsException extends RuntimeException {

    public CustomerAlreadyExistsException(String userId) {
        super("Customer already exists: " + userId);
    }
}