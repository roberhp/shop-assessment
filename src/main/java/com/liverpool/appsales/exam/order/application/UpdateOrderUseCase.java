package com.liverpool.appsales.exam.order.application;

import com.liverpool.appsales.exam.order.application.exception.OrderNotFoundException;
import com.liverpool.appsales.exam.order.domain.Order;
import org.springframework.stereotype.Service;

@Service
public class UpdateOrderUseCase {

    private final OrderRepository orderRepository;

    public UpdateOrderUseCase(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public Order execute(Order order) {

        Order existingOrder = orderRepository
                .findByOrderRef(order.getOrderRef())
                .orElseThrow(() -> new OrderNotFoundException(order.getOrderRef()));

        existingOrder.setUserId(order.getUserId());
        existingOrder.setCanal(order.getCanal());
        existingOrder.setStoreName(order.getStoreName());
        existingOrder.setEstimateDeliveryDate(order.getEstimateDeliveryDate());
        existingOrder.setItems(order.getItems());

        return orderRepository.save(existingOrder);
    }
}