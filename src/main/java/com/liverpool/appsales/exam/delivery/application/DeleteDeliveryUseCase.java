package com.liverpool.appsales.exam.delivery.application;

import com.liverpool.appsales.exam.delivery.application.exception.DeliveryNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DeleteDeliveryUseCase {

    private final DeliveryRepository deliveryRepository;

    public void execute(String deliveryId) {

        if (!deliveryRepository.existsByDeliveryId(deliveryId)) {
            throw new DeliveryNotFoundException(
                    "No existe la entrega: " + deliveryId
            );
        }

        deliveryRepository.deleteByDeliveryId(deliveryId);
    }
}