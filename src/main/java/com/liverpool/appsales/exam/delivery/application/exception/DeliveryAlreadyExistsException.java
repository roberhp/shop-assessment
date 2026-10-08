package com.liverpool.appsales.exam.delivery.application.exception;

public class DeliveryAlreadyExistsException extends RuntimeException {

    public DeliveryAlreadyExistsException(String deliveryId) {
        super("Delivery already exists: " + deliveryId);
    }
}