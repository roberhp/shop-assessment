package com.liverpool.appsales.exam.search.domain;

import com.liverpool.appsales.exam.item.domain.Item;
import com.liverpool.appsales.exam.order.domain.Order;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SearchResult {

    private List<Order> orders;
    private List<Item> items;
}