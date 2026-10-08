package com.liverpool.appsales.exam.delivery.presentation.dto;

import jakarta.validation.constraints.NotBlank;

public record CreateDeliveryRequest(

    @NotBlank
    String orderRef,

    @NotBlank
    String shippingAddress
) {
}