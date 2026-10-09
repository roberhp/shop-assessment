package com.liverpool.appsales.exam.delivery.application;

import com.liverpool.appsales.exam.delivery.application.exception.DeliveryNotFoundException;
import com.liverpool.appsales.exam.delivery.domain.Delivery;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UpdateDeliveryUseCaseTest {

    private final DeliveryRepository deliveryRepository = mock(DeliveryRepository.class);

    private final UpdateDeliveryUseCase useCase = new UpdateDeliveryUseCase(deliveryRepository);

    @Test
    void shouldUpdateDelivery() {

        Delivery existingDelivery = new Delivery(
                "delivery-1",
                "3010091676",
                "Old address"
        );

        Delivery updatedDelivery = new Delivery(
                "delivery-1",
                "3010091676",
                "New address"
        );

        when(deliveryRepository.findByDeliveryId("delivery-1"))
                .thenReturn(Optional.of(existingDelivery));

        when(deliveryRepository.save(existingDelivery))
                .thenReturn(existingDelivery);

        Delivery result = useCase.execute(updatedDelivery);

        assertEquals(
                "New address",
                result.getShippingAddress()
        );

        verify(deliveryRepository).save(existingDelivery);
    }

    @Test
    void shouldNotSaveWhenDeliveryDoesNotExist() {

        Delivery delivery = new Delivery(
                "delivery-1",
                "3010091676",
                "New address"
        );

        when(deliveryRepository.findByDeliveryId("delivery-1"))
                .thenReturn(Optional.empty());

        assertThrows(
                DeliveryNotFoundException.class,
                () -> useCase.execute(delivery)
        );

        verify(deliveryRepository, never()).save(any());
    }
}