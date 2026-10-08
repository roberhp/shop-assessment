package com.liverpool.appsales.exam.item.infrastructure;

import com.liverpool.appsales.exam.item.application.ItemRepository;
import com.liverpool.appsales.exam.item.domain.Item;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class ItemRepositoryAdapter implements ItemRepository {

    private final ItemMongoRepository itemMongoRepository;

    @Override
    public Item save(Item item) {
        ItemDocument document = new ItemDocument();
        document.setId(item.getId());
        document.setItemId(item.getItemId());
        document.setSkuId(item.getSkuId());
        document.setQuantity(item.getQuantity());
        document.setDisplayName(item.getDisplayName());
        document.setDeliveryStatus(item.getDeliveryStatus());

        ItemDocument savedDocument = itemMongoRepository.save(document);

        return toDomain(savedDocument);
    }

    @Override
    public boolean existsByItemId(String itemId) {
        return itemMongoRepository.existsByItemId(itemId);
    }

    @Override
    public List<Item> findAll() {
        return itemMongoRepository.findAll()
                .stream()
                .map(this::toDomain)
                .toList();
    }

    @Override
    public List<Item> findByItemIdIn(List<String> itemIds) {
        return itemMongoRepository.findByItemIdIn(itemIds)
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