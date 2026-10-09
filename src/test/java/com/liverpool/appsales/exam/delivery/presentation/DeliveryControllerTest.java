package com.liverpool.appsales.exam.delivery.presentation;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.liverpool.appsales.exam.delivery.application.CreateDeliveryUseCase;
import com.liverpool.appsales.exam.delivery.application.DeleteDeliveryUseCase;
import com.liverpool.appsales.exam.delivery.application.GetDeliveryUseCase;
import com.liverpool.appsales.exam.delivery.application.UpdateDeliveryUseCase;
import com.liverpool.appsales.exam.delivery.application.exception.DeliveryNotFoundException;
import com.liverpool.appsales.exam.delivery.domain.Delivery;
import com.liverpool.appsales.exam.delivery.presentation.dto.CreateDeliveryRequest;
import com.liverpool.appsales.exam.delivery.presentation.dto.UpdateDeliveryRequest;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(DeliveryController.class)
class DeliveryControllerTest {

        @Autowired
        private MockMvc mockMvc;

        private final ObjectMapper objectMapper = new ObjectMapper();

        @MockitoBean
        private CreateDeliveryUseCase createDeliveryUseCase;

        @MockitoBean
        private GetDeliveryUseCase getDeliveryUseCase;

        @MockitoBean
        private UpdateDeliveryUseCase updateDeliveryUseCase;

        @MockitoBean
        private DeleteDeliveryUseCase deleteDeliveryUseCase;

        @Test
        void shouldCreateDelivery() throws Exception {

                CreateDeliveryRequest request = new CreateDeliveryRequest(
                                "3010091676",
                                "Av. Insurgentes Sur 123, CDMX");

                Delivery delivery = new Delivery(
                                "delivery-123",
                                "3010091676",
                                "Av. Insurgentes Sur 123, CDMX");

                when(createDeliveryUseCase.execute(any(Delivery.class)))
                                .thenReturn(delivery);

                mockMvc.perform(post("/deliveries")
                                .contentType("application/json")
                                .content(objectMapper.writeValueAsString(request)))
                                .andExpect(status().isCreated())
                                .andExpect(jsonPath("$.deliveryId").value("delivery-123"))
                                .andExpect(jsonPath("$.orderRef").value("3010091676"))
                                .andExpect(jsonPath("$.shippingAddress")
                                                .value("Av. Insurgentes Sur 123, CDMX"));
        }

        @Test
        void shouldGetDelivery() throws Exception {

                Delivery delivery = new Delivery(
                                "delivery-123",
                                "3010091676",
                                "Av. Insurgentes Sur 123, CDMX");

                when(getDeliveryUseCase.execute("delivery-123"))
                                .thenReturn(delivery);

                mockMvc.perform(get("/deliveries/delivery-123"))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.deliveryId").value("delivery-123"))
                                .andExpect(jsonPath("$.orderRef").value("3010091676"));
        }

        @Test
        void shouldUpdateDelivery() throws Exception {

                UpdateDeliveryRequest request = new UpdateDeliveryRequest(
                                "3010091676",
                                "Av. Reforma 500, CDMX");

                Delivery delivery = new Delivery(
                                "delivery-123",
                                "3010091676",
                                "Av. Reforma 500, CDMX");

                when(updateDeliveryUseCase.execute(any(Delivery.class)))
                                .thenReturn(delivery);

                mockMvc.perform(put("/deliveries/delivery-123")
                                .contentType("application/json")
                                .content(objectMapper.writeValueAsString(request)))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.deliveryId").value("delivery-123"))
                                .andExpect(jsonPath("$.shippingAddress")
                                                .value("Av. Reforma 500, CDMX"));
        }

        @Test
        void shouldRejectInvalidCreateRequest() throws Exception {

                CreateDeliveryRequest request = new CreateDeliveryRequest(
                                "",
                                "");

                mockMvc.perform(post("/deliveries")
                                .contentType("application/json")
                                .content(objectMapper.writeValueAsString(request)))
                                .andExpect(status().isBadRequest());
        }

        @Test
        void shouldDeleteDelivery() throws Exception {

                Mockito.doNothing()
                                .when(deleteDeliveryUseCase)
                                .execute("delivery-001");

                mockMvc.perform(
                                delete("/deliveries/{deliveryId}", "delivery-001"))
                                .andExpect(status().isNoContent());

                Mockito.verify(deleteDeliveryUseCase)
                                .execute("delivery-001");
        }

        @Test
        void shouldReturnNotFoundWhenDeletingDelivery() throws Exception {

                Mockito.doThrow(
                                new DeliveryNotFoundException("Entrega no encontrada"))
                                .when(deleteDeliveryUseCase)
                                .execute("delivery-001");

                mockMvc.perform(
                                delete("/deliveries/{deliveryId}", "delivery-001"))
                                .andExpect(status().isNotFound());

                Mockito.verify(deleteDeliveryUseCase)
                                .execute("delivery-001");
        }
}