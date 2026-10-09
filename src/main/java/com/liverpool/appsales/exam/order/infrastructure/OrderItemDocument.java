package com.liverpool.appsales.exam.order.infrastructure;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderItemDocument {

    private String itemId;
    
    private String skuId;
    
    private Integer quantity;
}