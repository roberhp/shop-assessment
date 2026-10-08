package com.liverpool.appsales.exam.delivery.application;

import com.liverpool.appsales.exam.delivery.application.exception.DeliveryNotFoundException;
import com.liverpool.appsales.exam.delivery.domain.Delivery;
import org.springframework.stereotype.Service;

@Service
public class UpdateDeliveryUseCase {

    private final DeliveryRepository deliveryRepository;

    public UpdateDeliveryUseCase(DeliveryRepository deliveryRepository) {
        this.deliveryRepository = deliveryRepository;
    }

    public Delivery execute(Delivery delivery) {

        Delivery existingDelivery = deliveryRepository
                .findByDeliveryId(delivery.getDeliveryId())
                .orElseThrow(() -> new DeliveryNotFoundException(delivery.getDeliveryId()) );

        existingDelivery.setOrderRef(delivery.getOrderRef());
        existingDelivery.setShippingAddress(delivery.getShippingAddress());

        return deliveryRepository.save(existingDelivery);
    }
}