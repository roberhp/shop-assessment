package com.liverpool.appsales.exam.order.infrastructure;

import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface OrderMongoRepository
        extends MongoRepository<OrderDocument, String> {

    Optional<OrderDocument> findByOrderRef(String orderRef);

    boolean existsByOrderRef(String orderRef);
}