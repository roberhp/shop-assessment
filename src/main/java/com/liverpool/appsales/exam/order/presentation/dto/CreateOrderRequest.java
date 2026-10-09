package com.liverpool.appsales.exam.order.presentation.dto;

import com.liverpool.appsales.exam.order.domain.OrderItem;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

import java.time.LocalDate;
import java.util.List;

public record CreateOrderRequest(

    @NotBlank String orderRef,
    @NotBlank String userId,
    @NotBlank String canal,
    @NotBlank String orderStatus,
    String storeName,
    LocalDate estimateDeliveryDate,
    @NotEmpty List<OrderItem> items
) {
}