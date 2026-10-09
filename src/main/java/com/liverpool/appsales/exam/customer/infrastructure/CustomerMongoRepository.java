package com.liverpool.appsales.exam.customer.infrastructure;

import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface CustomerMongoRepository
        extends MongoRepository<CustomerDocument, String> {

    Optional<CustomerDocument> findByUserId(String userId);

    boolean existsByUserId(String userId);

    void deleteByUserId(String userId);
}