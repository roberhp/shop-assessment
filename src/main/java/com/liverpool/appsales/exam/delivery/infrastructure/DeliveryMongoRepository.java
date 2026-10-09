package com.liverpool.appsales.exam.delivery.infrastructure;

import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface DeliveryMongoRepository
        extends MongoRepository<DeliveryDocument, String> {

    Optional<DeliveryDocument> findByDeliveryId(String deliveryId);

    boolean existsByDeliveryId(String deliveryId);

    void deleteByDeliveryId(String deliveryId);
}