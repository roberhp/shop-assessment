package com.liverpool.appsales.exam.delivery.application.exception;

public class DeliveryNotFoundException extends RuntimeException {

    public DeliveryNotFoundException(String deliveryId) {
        super("Delivery not found: " + deliveryId);
    }
}