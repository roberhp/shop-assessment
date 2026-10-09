package com.liverpool.appsales.exam.delivery.application;

import com.liverpool.appsales.exam.delivery.domain.Delivery;

import java.util.Optional;

public interface DeliveryRepository {

    Delivery save(Delivery delivery);

    Optional<Delivery> findByDeliveryId(String deliveryId);

    boolean existsByDeliveryId(String deliveryId);

    void deleteByDeliveryId(String deliveryId);
}