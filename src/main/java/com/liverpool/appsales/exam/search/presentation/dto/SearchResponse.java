package com.liverpool.appsales.exam.search.presentation.dto;

import com.liverpool.appsales.exam.item.domain.Item;
import com.liverpool.appsales.exam.order.domain.Order;

import java.util.List;

public record SearchResponse(
        List<Order> orders,
        List<Item> items
) {
}