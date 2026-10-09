package com.liverpool.appsales.exam.order.application;

import com.liverpool.appsales.exam.order.application.exception.OrderAlreadyExistsException;
import com.liverpool.appsales.exam.order.domain.Order;
import com.liverpool.appsales.exam.order.domain.OrderItem;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CreateOrderUseCaseTest {

    private final OrderRepository orderRepository =
            mock(OrderRepository.class);

    private final CreateOrderUseCase useCase =
            new CreateOrderUseCase(orderRepository);

    @Test
    void shouldCreateOrder() {

        Order order = createOrder();

        when(orderRepository.existsByOrderRef(order.getOrderRef()))
                .thenReturn(false);

        when(orderRepository.save(order))
                .thenReturn(order);

        Order result = useCase.execute(order);

        assertEquals(order, result);
        verify(orderRepository).save(order);
    }

    @Test
    void shouldRejectExistingOrder() {

        Order order = createOrder();

        when(orderRepository.existsByOrderRef(order.getOrderRef()))
                .thenReturn(true);

        assertThrows(
                OrderAlreadyExistsException.class,
                () -> useCase.execute(order)
        );

        verify(orderRepository, never()).save(any());
    }

    private Order createOrder() {

        OrderItem item = new OrderItem(
                "3010091676-1132351437",
                "1132351437",
                1
        );

        return new Order(
                "3010091676",
                "75c97531-abf5-4524-8107-90aa48d08efc",
                "ONLINE",
                "PENDING",
                "Liverpool",
                LocalDate.of(2026, 10, 20),
                List.of(item)
        );
    }
}