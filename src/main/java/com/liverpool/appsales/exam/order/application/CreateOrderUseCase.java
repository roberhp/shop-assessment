package com.liverpool.appsales.exam.order.application;

import com.liverpool.appsales.exam.order.application.exception.OrderAlreadyExistsException;
import com.liverpool.appsales.exam.order.domain.Order;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class CreateOrderUseCase {

    private final OrderRepository orderRepository;

    public CreateOrderUseCase(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public Order execute(Order order) {

        if (orderRepository.existsByOrderRef(order.getOrderRef())) {
            throw new OrderAlreadyExistsException(order.getOrderRef());
        }

        if (order.getItems() == null) {
            order.setItems(new ArrayList<>());
        }

        return orderRepository.save(order);
    }
}