package com.liverpool.appsales.exam.delivery.presentation.dto;

public record DeliveryResponse(
    String deliveryId,
    String orderRef,
    String shippingAddress
) {
}