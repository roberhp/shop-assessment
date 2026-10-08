package com.liverpool.appsales.exam.order.domain;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Item associated with an order")
public class OrderItem {

    @Schema(description = "Identifier used to associate the item with the order", example = "3010091676-1132351437")
    private String itemId;

    @Schema(description = "Product SKU identifier", example = "1132351437")
    private String skuId;

    @Schema(description = "Quantity of the product", example = "2")
    private Integer quantity;
}