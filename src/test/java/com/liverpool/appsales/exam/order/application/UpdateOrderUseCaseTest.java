package com.liverpool.appsales.exam.order.application;

import com.liverpool.appsales.exam.order.application.exception.OrderNotFoundException;
import com.liverpool.appsales.exam.order.domain.Order;
import com.liverpool.appsales.exam.order.domain.OrderItem;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UpdateOrderUseCaseTest {

    private final OrderRepository orderRepository =
        mock(OrderRepository.class);

    private final UpdateOrderUseCase useCase =
        new UpdateOrderUseCase(orderRepository);

    @Test
    void shouldUpdateOrder() {

    Order existingOrder = new Order(
        "3010091676",
        "user-1",
        "ONLINE",
        "PENDING",
        "Store A",
        LocalDate.of(2026, 10, 20),
        List.of(
            new OrderItem(
                "3010091676-1132351437",
                "1132351437",
                1
            )
        )
    );

    Order updatedOrder = new Order(
        "3010091676",
        "user-1",
        "STORE",
        "PENDING",
        "Store B",
        LocalDate.of(2026, 10, 25),
        List.of(
            new OrderItem(
                "3010091676-1132351437",
                "1132351437",
                2
            )
        )
    );

    when(orderRepository.findByOrderRef("3010091676"))
        .thenReturn(Optional.of(existingOrder));

    when(orderRepository.save(existingOrder))
        .thenReturn(existingOrder);

    Order result = useCase.execute(updatedOrder);

    assertEquals("STORE", result.getCanal());
    assertEquals("Store B", result.getStoreName());
    assertEquals(
        LocalDate.of(2026, 10, 25),
        result.getEstimateDeliveryDate()
    );
    assertEquals(2, result.getItems().get(0).getQuantity());

    verify(orderRepository).save(existingOrder);
    }

    @Test
    void shouldThrowExceptionWhenOrderDoesNotExist() {

    Order order = new Order();
    order.setOrderRef("3010091676");

    when(orderRepository.findByOrderRef("3010091676"))
        .thenReturn(Optional.empty());

    assertThrows(
        OrderNotFoundException.class,
        () -> useCase.execute(order)
    );

    verify(orderRepository, never()).save(any());
    }
}