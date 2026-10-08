package com.liverpool.appsales.exam.order.infrastructure;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "orders")
public class OrderDocument {

    @Id
    private String id;

    private String orderRef;
    private String userId;
    private String canal;
    private String orderStatus;
    private String storeName;
    private LocalDate estimateDeliveryDate;
    private List<OrderItemDocument> items;
}