package com.liverpool.appsales.exam.customer.application;

import com.liverpool.appsales.exam.customer.application.exception.CustomerAlreadyExistsException;
import com.liverpool.appsales.exam.customer.domain.Customer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CreateCustomerUseCaseTest {

    @Mock
    private CustomerRepository customerRepository;

    private CreateCustomerUseCase createCustomerUseCase;

    @BeforeEach
    void setUp() {
        createCustomerUseCase =
                new CreateCustomerUseCase(customerRepository);
    }

    @Test
    void shouldCreateCustomer() {

        Customer customer = new Customer(
                "user-123",
                "Roberto",
                "Huerta",
                "Perez",
                "roberto@example.com",
                null
        );

        when(customerRepository.existsByUserId("user-123"))
                .thenReturn(false);

        when(customerRepository.save(any(Customer.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        Customer result = createCustomerUseCase.execute(customer);

        assertNotNull(result);
        assertEquals("user-123", result.getUserId());
        assertNotNull(result.getOrders());
        assertTrue(result.getOrders().isEmpty());

        verify(customerRepository).existsByUserId("user-123");
        verify(customerRepository).save(customer);
    }

    @Test
    void shouldRejectExistingCustomer() {

        Customer customer = new Customer(
                "user-123",
                "Roberto",
                "Huerta",
                "Perez",
                "roberto@example.com",
                null
        );

        when(customerRepository.existsByUserId("user-123"))
                .thenReturn(true);

        assertThrows(
                CustomerAlreadyExistsException.class,
                () -> createCustomerUseCase.execute(customer)
        );

        verify(customerRepository)
                .existsByUserId("user-123");

        verify(customerRepository, never())
                .save(any(Customer.class));
    }
}