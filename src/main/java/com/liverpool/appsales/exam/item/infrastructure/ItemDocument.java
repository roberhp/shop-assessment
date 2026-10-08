package com.liverpool.appsales.exam.item.infrastructure;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "items")
public class ItemDocument {

    @Id
    private String id;

    private String itemId;
    
    private String skuId;
    
    private Integer quantity;
    
    private String displayName;
    
    private String deliveryStatus;
}