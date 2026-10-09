package com.liverpool.appsales.exam.order.application.exception;

public class OrderNotFoundException extends RuntimeException {

    public OrderNotFoundException(String orderRef) {
        super("Order not found: " + orderRef);
    }
}