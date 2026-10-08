package com.liverpool.appsales.exam.order.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderItem {

    private String itemId;
    private String skuId;
    private Integer quantity;
}