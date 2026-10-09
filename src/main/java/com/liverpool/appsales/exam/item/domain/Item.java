package com.liverpool.appsales.exam.item.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Item {

    private String itemId;
    
    private String skuId;
    
    private Integer quantity;
    
    private String displayName;
    
    private String deliveryStatus;
    
    private String id;
}