package com.liverpool.appsales.exam.delivery.infrastructure;

import com.liverpool.appsales.exam.delivery.application.DeliveryRepository;
import com.liverpool.appsales.exam.delivery.domain.Delivery;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class DeliveryRepositoryAdapter implements DeliveryRepository {

    private final DeliveryMongoRepository mongoRepository;

    public DeliveryRepositoryAdapter(DeliveryMongoRepository mongoRepository) {
        this.mongoRepository = mongoRepository;
    }

    @Override
    public Delivery save(Delivery delivery) {

        DeliveryDocument document = mongoRepository
                .findByDeliveryId(delivery.getDeliveryId())
                .orElseGet(DeliveryDocument::new);

        document.setDeliveryId(delivery.getDeliveryId());
        document.setOrderRef(delivery.getOrderRef());
        document.setShippingAddress(delivery.getShippingAddress());

        DeliveryDocument savedDocument =
                mongoRepository.save(document);

        return toDomain(savedDocument);
    }

    @Override
    public Optional<Delivery> findByDeliveryId(String deliveryId) {

        return mongoRepository
                .findByDeliveryId(deliveryId)
                .map(this::toDomain);
    }

    @Override
    public boolean existsByDeliveryId(String deliveryId) {

        return mongoRepository.existsByDeliveryId(deliveryId);
    }

    @Override
    public void deleteByDeliveryId(String deliveryId) {
        mongoRepository.deleteByDeliveryId(deliveryId);
    }

    private Delivery toDomain(DeliveryDocument document) {

        return new Delivery(
                document.getDeliveryId(),
                document.getOrderRef(),
                document.getShippingAddress()
        );
    }
}