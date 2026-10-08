package com.liverpool.appsales.exam.item.application;

import com.liverpool.appsales.exam.item.domain.Item;
import java.util.List;

public interface ItemRepository {

    List<Item> findAll();
    List<Item> findByItemIdIn(List<String> itemIds);
}