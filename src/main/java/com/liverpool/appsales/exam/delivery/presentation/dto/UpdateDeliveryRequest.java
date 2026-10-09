package com.liverpool.appsales.exam.delivery.presentation.dto;

import jakarta.validation.constraints.NotBlank;

public record UpdateDeliveryRequest(

    @NotBlank
    String orderRef,

    @NotBlank
    String shippingAddress
) {
}