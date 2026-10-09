package com.liverpool.appsales.exam.customer.application;

import com.liverpool.appsales.exam.customer.application.exception.CustomerNotFoundException;
import com.liverpool.appsales.exam.customer.domain.Customer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GetCustomerUseCaseTest {

    @Mock
    private CustomerRepository customerRepository;

    private GetCustomerUseCase getCustomerUseCase;

    @BeforeEach
    void setUp() {
        getCustomerUseCase =
                new GetCustomerUseCase(customerRepository);
    }

    @Test
    void shouldReturnCustomer() {

        Customer customer = new Customer(
                "user-123",
                "Roberto",
                "Huerta",
                "Perez",
                "roberto@example.com",
                java.util.List.of("3010091676")
        );

        when(customerRepository.findByUserId("user-123"))
                .thenReturn(Optional.of(customer));

        Customer result = getCustomerUseCase.execute("user-123");

        assertEquals("user-123", result.getUserId());
        assertEquals("Roberto", result.getFirstName());
        assertEquals(
                java.util.List.of("3010091676"),
                result.getOrders()
        );

        verify(customerRepository)
                .findByUserId("user-123");
    }

    @Test
    void shouldThrowExceptionWhenCustomerDoesNotExist() {

        when(customerRepository.findByUserId("user-123"))
                .thenReturn(Optional.empty());

        assertThrows(
                CustomerNotFoundException.class,
                () -> getCustomerUseCase.execute("user-123")
        );

        verify(customerRepository)
                .findByUserId("user-123");
    }
}