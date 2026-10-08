package com.liverpool.appsales.exam.delivery.application;

import com.liverpool.appsales.exam.delivery.application.exception.DeliveryNotFoundException;
import com.liverpool.appsales.exam.delivery.domain.Delivery;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class GetDeliveryUseCaseTest {

    private final DeliveryRepository deliveryRepository = mock(DeliveryRepository.class);

    private final GetDeliveryUseCase useCase = new GetDeliveryUseCase(deliveryRepository);

    @Test
    void shouldReturnDeliveryWhenItExists() {

        Delivery delivery = new Delivery(
                "delivery-1",
                "3010091676",
                "Av. Insurgentes Sur 123"
        );

        when(deliveryRepository.findByDeliveryId("delivery-1"))
                .thenReturn(Optional.of(delivery));

        Delivery result = useCase.execute("delivery-1");

        assertEquals("delivery-1", result.getDeliveryId());
        assertEquals("3010091676", result.getOrderRef());
    }

    @Test
    void shouldThrowExceptionWhenDeliveryDoesNotExist() {

        when(deliveryRepository.findByDeliveryId("delivery-1"))
                .thenReturn(Optional.empty());

        assertThrows(
                DeliveryNotFoundException.class,
                () -> useCase.execute("delivery-1")
        );
    }
}