package com.liverpool.appsales.exam.order.presentation.dto;

import com.liverpool.appsales.exam.order.domain.OrderItem;

import java.time.LocalDate;
import java.util.List;

public record OrderResponse(
        String orderRef,
        String userId,
        String canal,
        String orderStatus,
        String storeName,
        LocalDate estimateDeliveryDate,
        List<OrderItem> items) {
}