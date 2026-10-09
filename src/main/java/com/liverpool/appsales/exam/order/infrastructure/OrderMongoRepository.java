package com.liverpool.appsales.exam.order.infrastructure;

import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface OrderMongoRepository
        extends MongoRepository<OrderDocument, String> {

    Optional<OrderDocument> findByOrderRef(String orderRef);

    boolean existsByOrderRef(String orderRef);

    List<OrderDocument> findByUserId(String userId);

    List<OrderDocument> findByOrderRefContainingIgnoreCase(
            String orderRef);

    List<OrderDocument> findByOrderStatusContainingIgnoreCase(
            String orderStatus);

    List<OrderDocument> findByStoreNameContainingIgnoreCase(
            String storeName);

    void deleteByOrderRef(String orderRef);
}