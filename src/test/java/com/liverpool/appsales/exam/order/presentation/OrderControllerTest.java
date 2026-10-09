package com.liverpool.appsales.exam.order.presentation;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.liverpool.appsales.exam.order.application.CreateOrderUseCase;
import com.liverpool.appsales.exam.order.application.DeleteOrderUseCase;
import com.liverpool.appsales.exam.order.application.GetOrderUseCase;
import com.liverpool.appsales.exam.order.application.UpdateOrderUseCase;
import com.liverpool.appsales.exam.order.application.exception.OrderNotFoundException;
import com.liverpool.appsales.exam.order.domain.Order;
import com.liverpool.appsales.exam.order.domain.OrderItem;
import com.liverpool.appsales.exam.order.presentation.dto.CreateOrderRequest;
import com.liverpool.appsales.exam.order.presentation.dto.UpdateOrderRequest;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(OrderController.class)
class OrderControllerTest {

        @Autowired
        private MockMvc mockMvc;

        private final ObjectMapper objectMapper = new ObjectMapper()
                        .registerModule(new JavaTimeModule());

        @MockitoBean
        private CreateOrderUseCase createOrderUseCase;

        @MockitoBean
        private GetOrderUseCase getOrderUseCase;

        @MockitoBean
        private UpdateOrderUseCase updateOrderUseCase;

        @MockitoBean
        private DeleteOrderUseCase deleteOrderUseCase;

        @Test
        void shouldCreateOrder() throws Exception {

                OrderItem item = new OrderItem(
                                "3010091676-1132351437",
                                "1132351437",
                                2);

                CreateOrderRequest request = new CreateOrderRequest(
                                "3010091676",
                                "user-123",
                                "WEB",
                                "PENDING",
                                "Liverpool",
                                LocalDate.of(2026, 10, 20),
                                List.of(item));

                Order order = new Order(
                                "3010091676",
                                "user-123",
                                "WEB",
                                "PENDING",
                                "Liverpool",
                                LocalDate.of(2026, 10, 20),
                                List.of(item));

                when(createOrderUseCase.execute(any(Order.class)))
                                .thenReturn(order);

                mockMvc.perform(post("/orders")
                                .contentType("application/json")
                                .content(objectMapper.writeValueAsString(request)))
                                .andExpect(status().isCreated())
                                .andExpect(jsonPath("$.orderRef").value("3010091676"))
                                .andExpect(jsonPath("$.userId").value("user-123"))
                                .andExpect(jsonPath("$.canal").value("WEB"))
                                .andExpect(jsonPath("$.orderStatus").value("PENDING"))
                                .andExpect(jsonPath("$.storeName").value("Liverpool"))
                                .andExpect(jsonPath("$.items[0].itemId")
                                                .value("3010091676-1132351437"));
        }

        @Test
        void shouldGetOrder() throws Exception {

                OrderItem item = new OrderItem(
                                "3010091676-1132351437",
                                "1132351437",
                                2);

                Order order = new Order(
                                "3010091676",
                                "user-123",
                                "WEB",
                                "PENDING",
                                "Liverpool",
                                LocalDate.of(2026, 10, 20),
                                List.of(item));

                when(getOrderUseCase.execute("3010091676"))
                                .thenReturn(order);

                mockMvc.perform(get("/orders/3010091676"))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.orderRef").value("3010091676"))
                                .andExpect(jsonPath("$.userId").value("user-123"))
                                .andExpect(jsonPath("$.orderStatus").value("PENDING"))
                                .andExpect(jsonPath("$.items[0].skuId")
                                                .value("1132351437"));
        }

        @Test
        void shouldUpdateOrder() throws Exception {

                OrderItem item = new OrderItem(
                                "3010091676-1132351437",
                                "1132351437",
                                3);

                UpdateOrderRequest request = new UpdateOrderRequest(
                                "user-123",
                                "WEB",
                                "SHIPPED",
                                "Liverpool",
                                LocalDate.of(2026, 10, 22),
                                List.of(item));

                Order order = new Order(
                                "3010091676",
                                "user-123",
                                "WEB",
                                "SHIPPED",
                                "Liverpool",
                                LocalDate.of(2026, 10, 22),
                                List.of(item));

                when(updateOrderUseCase.execute(any(Order.class)))
                                .thenReturn(order);

                mockMvc.perform(put("/orders/3010091676")
                                .contentType("application/json")
                                .content(objectMapper.writeValueAsString(request)))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.orderRef").value("3010091676"))
                                .andExpect(jsonPath("$.orderStatus").value("SHIPPED"))
                                .andExpect(jsonPath("$.items[0].quantity").value(3));
        }

        @Test
        void shouldRejectInvalidCreateRequest() throws Exception {

                CreateOrderRequest request = new CreateOrderRequest(
                                "",
                                "",
                                "",
                                "",
                                "",
                                LocalDate.of(2026, 10, 20),
                                List.of());

                mockMvc.perform(post("/orders")
                                .contentType("application/json")
                                .content(objectMapper.writeValueAsString(request)))
                                .andExpect(status().isBadRequest());
        }

        @Test
        void shouldDeleteOrder() throws Exception {

                Mockito.doNothing()
                                .when(deleteOrderUseCase)
                                .execute("order-123");

                mockMvc.perform(
                                delete("/orders/{orderRef}", "order-123"))
                                .andExpect(status().isNoContent());

                Mockito.verify(deleteOrderUseCase)
                                .execute("order-123");
        }

        @Test
        void shouldReturnNotFoundWhenDeletingOrder() throws Exception {

                Mockito.doThrow(new OrderNotFoundException("Pedido no encontrado"))
                                .when(deleteOrderUseCase)
                                .execute("order-123");

                mockMvc.perform(
                                delete("/orders/{orderRef}", "order-123"))
                                .andExpect(status().isNotFound());

                Mockito.verify(deleteOrderUseCase)
                                .execute("order-123");
        }
}