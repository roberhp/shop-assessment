package com.liverpool.appsales.exam.order.application;

import com.liverpool.appsales.exam.order.application.exception.OrderNotFoundException;
import com.liverpool.appsales.exam.order.domain.Order;
import org.springframework.stereotype.Service;

@Service
public class GetOrderUseCase {

    private final OrderRepository orderRepository;

    public GetOrderUseCase(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public Order execute(String orderRef) {

        return orderRepository.findByOrderRef(orderRef)
                .orElseThrow(() -> new OrderNotFoundException(orderRef));
    }
}