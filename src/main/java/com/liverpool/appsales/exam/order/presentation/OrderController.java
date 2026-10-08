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
import org.springframework.web.bind.annotation.*;

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

    @PostMapping
    public ResponseEntity<OrderResponse> create(@Valid @RequestBody CreateOrderRequest request) {

        Order order = new Order(
                request.orderRef(),
                request.userId(),
                request.canal(),
                request.orderStatus(),
                request.storeName(),
                request.estimateDeliveryDate(),
                request.items()
        );

        Order createdOrder = createOrderUseCase.execute(order);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(toResponse(createdOrder));
    }

    @GetMapping("/{orderRef}")
    public ResponseEntity<OrderResponse> get(@PathVariable String orderRef) {

        Order order = getOrderUseCase.execute(orderRef);

        return ResponseEntity.ok(toResponse(order));
    }

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
                request.items()
        );
        

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
                order.getItems()
        );
    }
}