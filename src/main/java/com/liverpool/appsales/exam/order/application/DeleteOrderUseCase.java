package com.liverpool.appsales.exam.order.application;

import com.liverpool.appsales.exam.order.application.exception.OrderNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DeleteOrderUseCase {

    private final OrderRepository orderRepository;

    public void execute(String orderRef) {

        if (!orderRepository.existsByOrderRef(orderRef)) {
            throw new OrderNotFoundException(
                    "No existe el pedido: " + orderRef
            );
        }

        orderRepository.deleteByOrderRef(orderRef);
    }
}