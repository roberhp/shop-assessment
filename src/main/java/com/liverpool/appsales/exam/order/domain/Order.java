package com.liverpool.appsales.exam.order.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Order {

    private String orderRef;
    private String userId;
    private String canal;
    private String orderStatus;
    private String storeName;
    private LocalDate estimateDeliveryDate;
    private List<OrderItem> items;
}