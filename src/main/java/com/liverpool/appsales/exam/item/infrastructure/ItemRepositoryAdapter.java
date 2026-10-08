package com.liverpool.appsales.exam.item.infrastructure;

import com.liverpool.appsales.exam.item.application.ItemRepository;
import com.liverpool.appsales.exam.item.domain.Item;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ItemRepositoryAdapter implements ItemRepository {

    private final ItemMongoRepository mongoRepository;

    public ItemRepositoryAdapter(ItemMongoRepository mongoRepository) {
        this.mongoRepository = mongoRepository;
    }

    @Override
    public List<Item> findAll() {
        return mongoRepository.findAll()
                .stream()
                .map(this::toDomain)
                .toList();
    }

    @Override
    public List<Item> findByItemIdIn(List<String> itemIds) {
        return mongoRepository.findByItemIdIn(itemIds)
                .stream()
                .map(this::toDomain)
                .toList();
    }

    private Item toDomain(ItemDocument document) {
        return new Item(
                document.getItemId(),
                document.getSkuId(),
                document.getQuantity(),
                document.getDisplayName(),
                document.getDeliveryStatus(),
                document.getId()
        );
    }
}