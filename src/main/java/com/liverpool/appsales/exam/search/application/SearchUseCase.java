package com.liverpool.appsales.exam.search.application;

import com.liverpool.appsales.exam.item.application.ItemRepository;
import com.liverpool.appsales.exam.item.domain.Item;
import com.liverpool.appsales.exam.order.application.OrderRepository;
import com.liverpool.appsales.exam.order.domain.Order;
import com.liverpool.appsales.exam.search.domain.SearchCriteria;
import com.liverpool.appsales.exam.search.domain.SearchResult;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SearchUseCase {

    private final OrderRepository orderRepository;

    private final ItemRepository itemRepository;

    private final TextNormalizer textNormalizer;

    private final FuzzyMatcher fuzzyMatcher;

    public SearchUseCase(
            OrderRepository orderRepository,
            ItemRepository itemRepository,
            TextNormalizer textNormalizer,
            FuzzyMatcher fuzzyMatcher) {
        this.orderRepository = orderRepository;
        this.itemRepository = itemRepository;
        this.textNormalizer = textNormalizer;
        this.fuzzyMatcher = fuzzyMatcher;
    }

    public SearchResult execute(SearchCriteria criteria) {
        List<Order> orders = searchOrders(criteria);
        List<Item> items = searchItems(criteria);

        return new SearchResult(orders, items);
    }

    public List<Order> searchOrders(SearchCriteria criteria) {

        return orderRepository.findAll()
                .stream()
                .filter(order -> matches(
                        order.getOrderRef(),
                        criteria.getOrderRef()))
                .filter(order -> matches(
                        order.getOrderStatus(),
                        criteria.getOrderStatus()))
                .filter(order -> matches(
                        order.getStoreName(),
                        criteria.getStoreName()))
                .toList();
    }

    public List<Item> searchItems(SearchCriteria criteria) {

        if (criteria.getDisplayName() == null ||
                criteria.getDisplayName().isBlank()) {
            return List.of();
        }

        String query = textNormalizer.normalize(criteria.getDisplayName());

        return itemRepository.findAll()
                .stream()
                .filter(item -> {
                    String normalizedName = textNormalizer.normalize(item.getDisplayName());
                    return fuzzyMatcher.matches(
                            normalizedName,
                            query);
                })
                .toList();
    }

    private boolean matches(String value, String filter) {

        if (filter == null || filter.isBlank()) {
            return true;
        }

        String normalizedValue = textNormalizer.normalize(value);

        String normalizedFilter = textNormalizer.normalize(filter);

        return normalizedValue.contains(normalizedFilter);
    }
}