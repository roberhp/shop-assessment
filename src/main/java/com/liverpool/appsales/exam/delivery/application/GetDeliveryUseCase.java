package com.liverpool.appsales.exam.delivery.application;

import com.liverpool.appsales.exam.delivery.application.exception.DeliveryNotFoundException;
import com.liverpool.appsales.exam.delivery.domain.Delivery;
import org.springframework.stereotype.Service;

@Service
public class GetDeliveryUseCase {

    private final DeliveryRepository deliveryRepository;

    public GetDeliveryUseCase(DeliveryRepository deliveryRepository) {
        this.deliveryRepository = deliveryRepository;
    }

    public Delivery execute(String deliveryId) {

        return deliveryRepository.findByDeliveryId(deliveryId)
                .orElseThrow(() -> new DeliveryNotFoundException(deliveryId));
    }
}