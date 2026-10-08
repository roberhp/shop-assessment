package com.liverpool.appsales.exam.item.infrastructure;

import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface ItemMongoRepository
        extends MongoRepository<ItemDocument, String> {

    List<ItemDocument> findByItemIdIn(List<String> itemIds);
}