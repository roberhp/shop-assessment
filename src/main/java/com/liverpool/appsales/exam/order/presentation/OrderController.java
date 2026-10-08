package com.liverpool.appsales.exam.order.presentation;

import com.liverpool.appsales.exam.order.application.CreateOrderUseCase;
import com.liverpool.appsales.exam.order.application.GetOrderUseCase;
import com.liverpool.appsales.exam.order.application.UpdateOrderUseCase;
import com.liverpool.appsales.exam.order.domain.Order;
import com.liverpool.appsales.exam.order.presentation.dto.CreateOrderRequest;
import com.liverpool.appsales.exam.order.presentation.dto.OrderResponse;
import com.liverpool.appsales.exam.order.presentation.dto.UpdateOrderRequest;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Orders", description = "Operations for order management")
@RestController
@RequestMapping("/orders")
public class OrderController {

    private final CreateOrderUseCase createOrderUseCase;

    private final GetOrderUseCase getOrderUseCase;

    private final UpdateOrderUseCase updateOrderUseCase;

    public OrderController(
            CreateOrderUseCase createOrderUseCase,
            GetOrderUseCase getOrderUseCase,
            UpdateOrderUseCase updateOrderUseCase) {

        this.createOrderUseCase = createOrderUseCase;
        this.getOrderUseCase = getOrderUseCase;
        this.updateOrderUseCase = updateOrderUseCase;
    }

    @Operation(summary = "Create an order", description = "Creates a new order with its associated items.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", 
            description = "Order created successfully", 
            content = @Content(schema = @Schema(implementation = OrderResponse.class))),
            @ApiResponse(responseCode = "400", 
            description = "Invalid request"),
            @ApiResponse(responseCode = "409", 
            description = "Order already exists")
    })
    @PostMapping
    public ResponseEntity<OrderResponse> create(@Valid @RequestBody CreateOrderRequest request) {

        Order order = new Order(
                request.orderRef(),
                request.userId(),
                request.canal(),
                request.orderStatus(),
                request.storeName(),
                request.estimateDeliveryDate(),
                request.items());

        Order createdOrder = createOrderUseCase.execute(order);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(toResponse(createdOrder));
    }

    @Operation(summary = "Get an order", description = "Retrieves an order using its order reference.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", 
            description = "Order found", 
            content = @Content(schema = @Schema(implementation = OrderResponse.class))),
            @ApiResponse(responseCode = "404", 
            description = "Order not found")
    })
    @GetMapping("/{orderRef}")
    public ResponseEntity<OrderResponse> get(
            @Parameter(description = "Unique order reference", 
                required = true, 
                example = "3010091676") 
            @PathVariable String orderRef) {

        Order order = getOrderUseCase.execute(orderRef);

        return ResponseEntity.ok(toResponse(order));
    }

    @Operation(summary = "Update an order", description = "Updates an existing order.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", 
            description = "Order updated successfully", 
            content = @Content(schema = @Schema(implementation = OrderResponse.class))),
            @ApiResponse(responseCode = "400", 
            description = "Invalid request"),
            @ApiResponse(responseCode = "404", 
            description = "Order not found")
    })
    @PutMapping("/{orderRef}")
    public ResponseEntity<OrderResponse> update(
            @PathVariable String orderRef,
            @Valid @RequestBody UpdateOrderRequest request) {

        Order order = new Order(
                orderRef,
                request.userId(),
                request.canal(),
                request.orderStatus(),
                request.storeName(),
                request.estimateDeliveryDate(),
                request.items());

        Order updatedOrder = updateOrderUseCase.execute(order);

        return ResponseEntity.ok(toResponse(updatedOrder));
    }

    private OrderResponse toResponse(Order order) {
        return new OrderResponse(
                order.getOrderRef(),
                order.getUserId(),
                order.getCanal(),
                order.getOrderStatus(),
                order.getStoreName(),
                order.getEstimateDeliveryDate(),
                order.getItems());
    }
}