package com.liverpool.appsales.exam.customer.application.exception;

public class InvalidCustomerOrdersException extends RuntimeException {

    public InvalidCustomerOrdersException(String message) {
        super(message);
    }
}