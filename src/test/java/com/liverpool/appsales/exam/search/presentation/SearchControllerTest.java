package com.liverpool.appsales.exam.search.presentation;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.liverpool.appsales.exam.item.domain.Item;
import com.liverpool.appsales.exam.order.domain.Order;
import com.liverpool.appsales.exam.order.domain.OrderItem;
import com.liverpool.appsales.exam.search.application.SearchUseCase;
import com.liverpool.appsales.exam.search.domain.SearchResult;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(SearchController.class)
class SearchControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper()
        .registerModule(new JavaTimeModule());

    @MockitoBean
    private SearchUseCase searchUseCase;

    @Test
    void shouldSearchOrders() throws Exception {

        Order order = new Order(
                "3010091676",
                "user-123",
                "WEB",
                "PENDING",
                "Liverpool",
                LocalDate.of(2026, 10, 20),
                List.of(
                        new OrderItem(
                                "3010091676-1132351437",
                                "1132351437",
                                2
                        )
                )
        );

        SearchResult result = new SearchResult(
                List.of(order),
                List.of()
        );

        when(searchUseCase.execute(any()))
                .thenReturn(result);

        mockMvc.perform(get("/search")
                        .param("orderRef", "3010091676"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.orders[0].orderRef")
                        .value("3010091676"));
    }

    @Test
    void shouldSearchItems() throws Exception {

        Item item = new Item(
                "3010091676-1132351437",
                "1132351437",
                2,
                "Pantalón Levi's",
                "AVAILABLE",
                "1"
        );

        SearchResult result = new SearchResult(
                List.of(),
                List.of(item)
        );

        when(searchUseCase.execute(any()))
                .thenReturn(result);

        mockMvc.perform(get("/search")
                        .param("displayName", "Pantalon"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.items[0].itemId")
                        .value("3010091676-1132351437"))
                .andExpect(jsonPath("$.items[0].displayName")
                        .value("Pantalón Levi's"));
    }

    @Test
    void shouldReturnEmptySearch() throws Exception {

        SearchResult result = new SearchResult(
                List.of(),
                List.of()
        );

        when(searchUseCase.execute(any()))
                .thenReturn(result);

        mockMvc.perform(get("/search"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.orders").isArray())
                .andExpect(jsonPath("$.items").isArray())
                .andExpect(jsonPath("$.orders").isEmpty())
                .andExpect(jsonPath("$.items").isEmpty());
    }
}