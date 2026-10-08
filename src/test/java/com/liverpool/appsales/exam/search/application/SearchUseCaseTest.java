package com.liverpool.appsales.exam.search.application;

import com.liverpool.appsales.exam.item.application.ItemRepository;
import com.liverpool.appsales.exam.item.domain.Item;
import com.liverpool.appsales.exam.order.application.OrderRepository;
import com.liverpool.appsales.exam.order.domain.Order;
import com.liverpool.appsales.exam.search.domain.SearchCriteria;
import com.liverpool.appsales.exam.search.domain.SearchResult;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SearchUseCaseTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private ItemRepository itemRepository;

    private SearchUseCase searchUseCase;

    private Order orderOne;
    private Order orderTwo;

    private Item itemOne;
    private Item itemTwo;
    private Item itemThree;

    @BeforeEach
    void setUp() {
        TextNormalizer textNormalizer = new TextNormalizer();
        FuzzyMatcher fuzzyMatcher = new FuzzyMatcher();

        searchUseCase = new SearchUseCase(
                orderRepository,
                itemRepository,
                textNormalizer,
                fuzzyMatcher);

        orderOne = new Order(
                "3010091676",
                "user-1",
                "online",
                "2025-12-06",
                "L SANTA FE",
                LocalDate.of(2025, 12, 10),
                List.of());

        orderTwo = new Order(
                "4550129455",
                "user-2",
                "online",
                "2025-12-05",
                "Liverpool Parque Puebla",
                LocalDate.of(2025, 12, 9),
                List.of());

        itemOne = new Item(
                "3010091676-1132351437",
                "1132351437",
                3,
                "Pantalón Levi's",
                "Compra en línea",
                "1");

        itemTwo = new Item(
                "3010091676-1179743767",
                "1179743767",
                4,
                "Vasos cristal 250ml",
                "Compra en tienda",
                "2");

        itemThree = new Item(
                "4550129455-ci55667397890",
                "ci55667397890",
                1,
                "Monitor Acer 24 pulgadas",
                "Pedido entregado",
                "9");
    }

    @Test
    void shouldReturnAllOrdersAndAssociatedItemsWhenNoFiltersAreProvided() {
        SearchCriteria criteria = new SearchCriteria(
                null,
                null,
                null,
                null);

        when(orderRepository.findAll())
                .thenReturn(List.of(orderOne, orderTwo));

        when(itemRepository.findAll())
                .thenReturn(List.of(itemOne, itemTwo, itemThree));

        SearchResult result = searchUseCase.execute(criteria);

        assertEquals(2, result.getOrders().size());
        assertEquals(3, result.getItems().size());

        assertTrue(result.getOrders().contains(orderOne));
        assertTrue(result.getOrders().contains(orderTwo));

        assertTrue(result.getItems().contains(itemOne));
        assertTrue(result.getItems().contains(itemTwo));
        assertTrue(result.getItems().contains(itemThree));
    }

    @Test
    void shouldReturnOrdersAndTheirItemsWhenOrderRefIsProvided() {
        SearchCriteria criteria = new SearchCriteria(
                "3010091676",
                null,
                null,
                null);

        when(orderRepository.findAll())
                .thenReturn(List.of(orderOne, orderTwo));

        when(itemRepository.findAll())
                .thenReturn(List.of(itemOne, itemTwo, itemThree));

        SearchResult result = searchUseCase.execute(criteria);

        assertEquals(1, result.getOrders().size());
        assertEquals(
                "3010091676",
                result.getOrders().get(0).getOrderRef());

        assertEquals(2, result.getItems().size());
        assertTrue(result.getItems().contains(itemOne));
        assertTrue(result.getItems().contains(itemTwo));
    }

    @Test
    void shouldReturnOrdersAndTheirItemsWhenOrderStatusIsProvided() {
        SearchCriteria criteria = new SearchCriteria(
                null,
                "2025-12-05",
                null,
                null);

        when(orderRepository.findAll())
                .thenReturn(List.of(orderOne, orderTwo));

        when(itemRepository.findAll())
                .thenReturn(List.of(itemOne, itemTwo, itemThree));

        SearchResult result = searchUseCase.execute(criteria);

        assertEquals(1, result.getOrders().size());
        assertEquals(
                "4550129455",
                result.getOrders().get(0).getOrderRef());

        assertEquals(1, result.getItems().size());
        assertEquals(itemThree, result.getItems().get(0));
    }

    @Test
    void shouldReturnOrdersAndTheirItemsWhenStoreNameIsProvided() {
        SearchCriteria criteria = new SearchCriteria(
                null,
                null,
                "L SANTA FE",
                null);

        when(orderRepository.findAll())
                .thenReturn(List.of(orderOne, orderTwo));

        when(itemRepository.findAll())
                .thenReturn(List.of(itemOne, itemTwo, itemThree));

        SearchResult result = searchUseCase.execute(criteria);

        assertEquals(1, result.getOrders().size());
        assertEquals(
                "3010091676",
                result.getOrders().get(0).getOrderRef());

        assertEquals(2, result.getItems().size());
        assertTrue(result.getItems().contains(itemOne));
        assertTrue(result.getItems().contains(itemTwo));
    }

    @Test
    void shouldFilterItemsByDisplayNameAfterFilteringOrders() {
        SearchCriteria criteria = new SearchCriteria(
                "3010091676",
                null,
                null,
                "Pantalón");

        when(orderRepository.findAll())
                .thenReturn(List.of(orderOne, orderTwo));

        when(itemRepository.findAll())
                .thenReturn(List.of(itemOne, itemTwo, itemThree));

        SearchResult result = searchUseCase.execute(criteria);

        assertEquals(1, result.getOrders().size());
        assertEquals(
                "3010091676",
                result.getOrders().get(0).getOrderRef());

        assertEquals(1, result.getItems().size());
        assertEquals(itemOne, result.getItems().get(0));
    }

    @Test
    void shouldFindOrdersFromItemsWhenOnlyDisplayNameIsProvided() {
        SearchCriteria criteria = new SearchCriteria(
                null,
                null,
                null,
                "Monitor");

        when(orderRepository.findAll())
                .thenReturn(List.of(orderOne, orderTwo));

        when(itemRepository.findAll())
                .thenReturn(List.of(itemOne, itemTwo, itemThree));

        SearchResult result = searchUseCase.execute(criteria);

        assertEquals(1, result.getItems().size());
        assertEquals(itemThree, result.getItems().get(0));

        assertEquals(1, result.getOrders().size());
        assertEquals(
                "4550129455",
                result.getOrders().get(0).getOrderRef());
    }

    @Test
    void shouldApplyOrderFiltersBeforeDisplayNameFilter() {
        SearchCriteria criteria = new SearchCriteria(
                "3010091676",
                null,
                null,
                "Vasos");

        when(orderRepository.findAll())
                .thenReturn(List.of(orderOne, orderTwo));

        when(itemRepository.findAll())
                .thenReturn(List.of(itemOne, itemTwo, itemThree));

        SearchResult result = searchUseCase.execute(criteria);

        assertEquals(1, result.getOrders().size());
        assertEquals(
                "3010091676",
                result.getOrders().get(0).getOrderRef());

        assertEquals(1, result.getItems().size());
        assertEquals(itemTwo, result.getItems().get(0));

        assertTrue(result.getItems().stream()
                .noneMatch(item -> item.equals(itemThree)));
    }

    @Test
    void shouldReturnEmptyResultWhenDisplayNameDoesNotMatch() {
        SearchCriteria criteria = new SearchCriteria(
                null,
                null,
                null,
                "Producto inexistente");

        when(orderRepository.findAll())
                .thenReturn(List.of(orderOne, orderTwo));

        when(itemRepository.findAll())
                .thenReturn(List.of(itemOne, itemTwo, itemThree));

        SearchResult result = searchUseCase.execute(criteria);

        assertTrue(result.getOrders().isEmpty());
        assertTrue(result.getItems().isEmpty());
    }

    @Test
    void shouldIgnoreItemWithInvalidItemIdWhenSearchingByDisplayName() {
        Item invalidItem = new Item(
                "invalid-item-id",
                "999",
                1,
                "Producto especial",
                "Compra en línea",
                "12");

        SearchCriteria criteria = new SearchCriteria(
                null,
                null,
                null,
                "Producto especial");

        when(orderRepository.findAll())
                .thenReturn(List.of(orderOne, orderTwo));

        when(itemRepository.findAll())
                .thenReturn(List.of(invalidItem));

        SearchResult result = searchUseCase.execute(criteria);

        assertTrue(result.getOrders().isEmpty());
        assertEquals(1, result.getItems().size());
        assertEquals(invalidItem, result.getItems().get(0));
    }
}