package com.liverpool.appsales.exam.item.application;

import com.liverpool.appsales.exam.item.domain.Item;

import java.util.List;

public interface ItemRepository {

    Item save(Item item);

    boolean existsByItemId(String itemId);

    List<Item> findAll();

    List<Item> findByItemIdIn(List<String> itemIds);
}