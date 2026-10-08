package com.liverpool.appsales.exam.delivery.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Delivery {

    private String deliveryId;
    
    private String orderRef;
    
    private String shippingAddress;
}