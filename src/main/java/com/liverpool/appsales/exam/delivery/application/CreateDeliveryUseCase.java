package com.liverpool.appsales.exam.delivery.application;

import com.liverpool.appsales.exam.delivery.domain.Delivery;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class CreateDeliveryUseCase {

    private final DeliveryRepository deliveryRepository;

    public CreateDeliveryUseCase(DeliveryRepository deliveryRepository) {
        this.deliveryRepository = deliveryRepository;
    }

    public Delivery execute(Delivery delivery) {

        String deliveryId = UUID.randomUUID().toString();

        Delivery deliveryToSave = new Delivery(
                deliveryId,
                delivery.getOrderRef(),
                delivery.getShippingAddress()
        );

        return deliveryRepository.save(deliveryToSave);
    }
}