package com.liverpool.appsales.exam.order.application;

import com.liverpool.appsales.exam.order.domain.Order;

import java.util.List;
import java.util.Optional;

public interface OrderRepository {

    Order save(Order order);

    Optional<Order> findByOrderRef(String orderRef);

    boolean existsByOrderRef(String orderRef);

    List<Order> findAll();
}