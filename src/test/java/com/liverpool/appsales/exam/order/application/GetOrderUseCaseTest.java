package com.liverpool.appsales.exam.order.application;

import com.liverpool.appsales.exam.order.application.exception.OrderNotFoundException;
import com.liverpool.appsales.exam.order.domain.Order;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class GetOrderUseCaseTest {

    private final OrderRepository orderRepository = mock(OrderRepository.class);

    private final GetOrderUseCase useCase = new GetOrderUseCase(orderRepository);

    @Test
    void shouldReturnOrderWhenFound() {

        Order order = new Order();
        order.setOrderRef("3010091676");

        when(orderRepository.findByOrderRef("3010091676"))
                .thenReturn(Optional.of(order));

        Order result = useCase.execute("3010091676");

        assertEquals(order, result);
    }

    @Test
    void shouldThrowExceptionWhenOrderNotFound() {

        when(orderRepository.findByOrderRef("3010091676"))
                .thenReturn(Optional.empty());

        assertThrows(
                OrderNotFoundException.class,
                () -> useCase.execute("3010091676")
        );
    }
}