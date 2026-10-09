package com.liverpool.appsales.exam.customer.presentation;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.liverpool.appsales.exam.customer.application.CreateCustomerUseCase;
import com.liverpool.appsales.exam.customer.application.DeleteCustomerUseCase;
import com.liverpool.appsales.exam.customer.application.GetCustomerUseCase;
import com.liverpool.appsales.exam.customer.application.UpdateCustomerUseCase;
import com.liverpool.appsales.exam.customer.application.exception.CustomerNotFoundException;
import com.liverpool.appsales.exam.customer.domain.Customer;
import com.liverpool.appsales.exam.customer.presentation.dto.CreateCustomerRequest;
import com.liverpool.appsales.exam.customer.presentation.dto.UpdateCustomerRequest;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CustomerController.class)
class CustomerControllerTest {

    @Autowired
    private MockMvc mockMvc;

        private final ObjectMapper objectMapper = new ObjectMapper();

    @MockitoBean
    private CreateCustomerUseCase createCustomerUseCase;

    @MockitoBean
    private GetCustomerUseCase getCustomerUseCase;

    @MockitoBean
    private UpdateCustomerUseCase updateCustomerUseCase;

    @MockitoBean
    private DeleteCustomerUseCase deleteCustomerUseCase;

    @Test
    void shouldCreateCustomer() throws Exception {

        CreateCustomerRequest request = new CreateCustomerRequest(
                "user-123",
                "Juan",
                "Pérez",
                "López",
                "juan@example.com");

        Customer customer = new Customer(
                "user-123",
                "Juan",
                "Pérez",
                "López",
                "juan@example.com",
                List.of());

        when(createCustomerUseCase.execute(any(Customer.class)))
                .thenReturn(customer);

        mockMvc.perform(post("/customers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.userId").value("user-123"))
                .andExpect(jsonPath("$.firstName").value("Juan"))
                .andExpect(jsonPath("$.paternalLastName").value("Pérez"))
                .andExpect(jsonPath("$.maternalLastName").value("López"))
                .andExpect(jsonPath("$.email").value("juan@example.com"));
    }

    @Test
    void shouldGetCustomer() throws Exception {

        Customer customer = new Customer(
                "user-123",
                "Juan",
                "Pérez",
                "López",
                "juan@example.com",
                List.of("3010091676"));

        when(getCustomerUseCase.execute("user-123"))
                .thenReturn(customer);

        mockMvc.perform(get("/customers/user-123"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userId").value("user-123"))
                .andExpect(jsonPath("$.firstName").value("Juan"))
                .andExpect(jsonPath("$.orders[0]").value("3010091676"));
    }

    @Test
    void shouldUpdateCustomerAndAssociateOrders() throws Exception {

        List<String> orders = List.of(
                "3010091676",
                "30100916760987"
        );

        UpdateCustomerRequest request = new UpdateCustomerRequest(
                "Juan Carlos",
                "Pérez",
                "López",
                "juan.carlos@example.com",
                orders);

        Customer customer = new Customer(
                "user-123",
                "Juan Carlos",
                "Pérez",
                "López",
                "juan.carlos@example.com",
                orders);

        when(updateCustomerUseCase.execute(any(Customer.class)))
                .thenReturn(customer);

        mockMvc.perform(put("/customers/user-123")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userId").value("user-123"))
                .andExpect(jsonPath("$.firstName").value("Juan Carlos"))
                .andExpect(jsonPath("$.email").value("juan.carlos@example.com"))
                .andExpect(jsonPath("$.orders[0]").value("3010091676"))
                .andExpect(jsonPath("$.orders[1]").value("30100916760987"));

        ArgumentCaptor<Customer> captor =
                ArgumentCaptor.forClass(Customer.class);

        verify(updateCustomerUseCase).execute(captor.capture());

        assertEquals(
                "user-123",
                captor.getValue().getUserId());

        assertEquals(
                orders,
                captor.getValue().getOrders());
    }

    @Test
    void shouldRejectInvalidCreateRequest() throws Exception {

        CreateCustomerRequest request = new CreateCustomerRequest(
                "",
                "",
                "Pérez",
                "López",
                "invalid-email");

        mockMvc.perform(post("/customers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldDeleteCustomer() throws Exception {

        Mockito.doNothing()
                .when(deleteCustomerUseCase)
                .execute("user-123");

        mockMvc.perform(
                        delete("/customers/{userId}", "user-123"))
                .andExpect(status().isNoContent());

        Mockito.verify(deleteCustomerUseCase)
                .execute("user-123");
    }

    @Test
    void shouldReturnNotFoundWhenDeletingCustomer() throws Exception {

        Mockito.doThrow(new CustomerNotFoundException(
                        "Cliente no encontrado"))
                .when(deleteCustomerUseCase)
                .execute("user-123");

        mockMvc.perform(
                        delete("/customers/{userId}", "user-123"))
                .andExpect(status().isNotFound());

        Mockito.verify(deleteCustomerUseCase)
                .execute("user-123");
    }
}