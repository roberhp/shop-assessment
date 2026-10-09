package com.liverpool.appsales.exam.delivery.application;

import com.liverpool.appsales.exam.delivery.domain.Delivery;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CreateDeliveryUseCaseTest {

    private final DeliveryRepository deliveryRepository = mock(DeliveryRepository.class);

    private final CreateDeliveryUseCase useCase = new CreateDeliveryUseCase(deliveryRepository); 

    @Test
    void shouldGenerateDeliveryIdAndSaveDelivery() {

        Delivery delivery = new Delivery(
                null,
                "3010091676",
                "Av. Insurgentes Sur 123"
        );

        when(deliveryRepository.save(any(Delivery.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        Delivery result = useCase.execute(delivery);

        assertNotNull(result.getDeliveryId());
        assertEquals("3010091676", result.getOrderRef());
        assertEquals(
                "Av. Insurgentes Sur 123",
                result.getShippingAddress()
        );

        verify(deliveryRepository).save(any(Delivery.class));
    }
}