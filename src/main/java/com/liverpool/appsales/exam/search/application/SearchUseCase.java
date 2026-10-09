package com.liverpool.appsales.exam.search.application;

import com.liverpool.appsales.exam.item.application.ItemRepository;
import com.liverpool.appsales.exam.item.domain.Item;
import com.liverpool.appsales.exam.order.application.OrderRepository;
import com.liverpool.appsales.exam.order.domain.Order;
import com.liverpool.appsales.exam.search.domain.SearchCriteria;
import com.liverpool.appsales.exam.search.domain.SearchResult;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SearchUseCase {

    private final OrderRepository orderRepository;
    
    private final ItemRepository itemRepository;
    
    private final TextNormalizer textNormalizer;
    
    private final FuzzyMatcher fuzzyMatcher;

    public SearchResult execute(SearchCriteria criteria) {

        boolean hasOrderFilters = hasOrderFilters(criteria);
        boolean hasDisplayNameFilter = hasText(criteria.getDisplayName());

        if (hasOrderFilters) {
            List<Order> filteredOrders = findOrdersByCriteria(criteria);

            return searchFromOrders(
                    filteredOrders,
                    criteria);
        }

        if (hasDisplayNameFilter) {
            return searchFromDisplayName(criteria);
        }

        return searchAll();
    }

    private List<Order> findOrdersByCriteria(SearchCriteria criteria) {

        List<Order> orders = null;

        if (hasText(criteria.getOrderRef())) {
            orders = orderRepository.findByOrderRefContaining(
                    criteria.getOrderRef());
        }

        if (hasText(criteria.getOrderStatus())) {
            orders = intersect(
                    orders,
                    orderRepository.findByOrderStatusContaining(
                            criteria.getOrderStatus()));
        }

        if (hasText(criteria.getStoreName())) {
            orders = intersect(
                    orders,
                    orderRepository.findByStoreNameContaining(
                            criteria.getStoreName()));
        }

        return orders != null
                ? orders
                : Collections.emptyList();
    }

    private SearchResult searchFromOrders(
            List<Order> orders,
            SearchCriteria criteria) {

        Set<String> orderRefs = orders.stream()
                .map(Order::getOrderRef)
                .collect(Collectors.toSet());

        List<Item> items = findItemsByOrders(orderRefs);

        if (hasText(criteria.getDisplayName())) {
            items = filterItemsByDisplayName(
                    items,
                    criteria.getDisplayName());
        }

        return new SearchResult(orders, items);
    }

    private SearchResult searchFromDisplayName(
            SearchCriteria criteria) {

        List<Item> matchingItems = filterItemsByDisplayName(
                itemRepository.findAll(),
                criteria.getDisplayName());

        if (matchingItems.isEmpty()) {
            return new SearchResult(
                    Collections.emptyList(),
                    Collections.emptyList());
        }

        Set<String> orderRefs = matchingItems.stream()
                .map(this::extractOrderRef)
                .filter(ref -> ref != null && !ref.isBlank())
                .collect(Collectors.toSet());

        List<Order> orders = orderRepository.findAll().stream()
                .filter(order -> orderRefs.contains(order.getOrderRef()))
                .toList();

        return new SearchResult(
                orders,
                matchingItems);
    }

    private SearchResult searchAll() {

        List<Order> orders = orderRepository.findAll();

        Set<String> orderRefs = orders.stream()
                .map(Order::getOrderRef)
                .collect(Collectors.toSet());

        List<Item> items = findItemsByOrders(orderRefs);

        return new SearchResult(orders, items);
    }

    private List<Item> filterItemsByDisplayName(
            List<Item> items,
            String displayName) {

        if (!hasText(displayName)) {
            return items;
        }

        String normalizedQuery =
                textNormalizer.normalize(displayName);

        return items.stream()
                .filter(item -> item.getDisplayName() != null)
                .filter(item -> fuzzyMatcher.matches(
                        textNormalizer.normalize(item.getDisplayName()),
                        normalizedQuery))
                .toList();
    }

    private List<Item> findItemsByOrders(Set<String> orderRefs) {

        if (orderRefs.isEmpty()) {
            return Collections.emptyList();
        }

        List<Item> allItems = itemRepository.findAll();

        return allItems.stream()
                .filter(item -> {
                    String orderRef = extractOrderRef(item);

                    return orderRef != null
                            && orderRefs.contains(orderRef);
                })
                .toList();
    }

    private List<Order> intersect(
            List<Order> current,
            List<Order> next) {

        if (current == null) {
            return next;
        }

        Set<String> nextOrderRefs = next.stream()
                .map(Order::getOrderRef)
                .collect(Collectors.toSet());

        return current.stream()
                .filter(order ->
                        nextOrderRefs.contains(order.getOrderRef()))
                .toList();
    }

    private String extractOrderRef(Item item) {

        if (item == null || item.getItemId() == null) {
            return null;
        }

        String itemId = item.getItemId();

        int separatorIndex = itemId.lastIndexOf('-');

        if (separatorIndex <= 0) {
            return null;
        }

        return itemId.substring(0, separatorIndex);
    }

    private boolean hasOrderFilters(SearchCriteria criteria) {

        return hasText(criteria.getOrderRef())
                || hasText(criteria.getOrderStatus())
                || hasText(criteria.getStoreName());
    }

    private boolean hasText(String value) {
        return value != null && !value.isBlank();
    }
}