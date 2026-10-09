package com.liverpool.appsales.exam.order.application.exception;

public class OrderAlreadyExistsException extends RuntimeException {

    public OrderAlreadyExistsException(String orderRef) {
        super("Order already exists: " + orderRef);
    }
}