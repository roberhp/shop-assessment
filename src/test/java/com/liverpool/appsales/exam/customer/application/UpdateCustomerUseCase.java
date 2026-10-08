package com.liverpool.appsales.exam.customer.application;

import com.liverpool.appsales.exam.customer.application.exception.CustomerNotFoundException;
import com.liverpool.appsales.exam.customer.domain.Customer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UpdateCustomerUseCaseTest {

    @Mock
    private CustomerRepository customerRepository;

    private UpdateCustomerUseCase updateCustomerUseCase;

    @BeforeEach
    void setUp() {
        updateCustomerUseCase =
                new UpdateCustomerUseCase(customerRepository);
    }

    @Test
    void shouldUpdateCustomerAndPreserveOrders() {

        Customer existingCustomer = new Customer(
                "user-123",
                "Roberto",
                "Huerta",
                "Perez",
                "old@example.com",
                List.of(
                        "3010091676",
                        "30100916760987"
                )
        );

        Customer customerToUpdate = new Customer(
                "user-123",
                "Roberto",
                "NuevoApellido",
                "NuevoMaterno",
                "new@example.com",
                null
        );

        when(customerRepository.findByUserId("user-123"))
                .thenReturn(Optional.of(existingCustomer));

        when(customerRepository.save(any(Customer.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        Customer result =
                updateCustomerUseCase.execute(customerToUpdate);

        assertEquals("Roberto", result.getFirstName());
        assertEquals("NuevoApellido", result.getPaternalLastName());
        assertEquals("NuevoMaterno", result.getMaternalLastName());
        assertEquals("new@example.com", result.getEmail());

        assertEquals(
                List.of(
                        "3010091676",
                        "30100916760987"
                ),
                result.getOrders()
        );

        ArgumentCaptor<Customer> captor =
                ArgumentCaptor.forClass(Customer.class);

        verify(customerRepository).save(captor.capture());

        Customer savedCustomer = captor.getValue();

        assertEquals(
                List.of(
                        "3010091676",
                        "30100916760987"
                ),
                savedCustomer.getOrders()
        );
    }

    @Test
    void shouldThrowExceptionWhenCustomerDoesNotExist() {

        Customer customer = new Customer(
                "user-123",
                "Roberto",
                "Huerta",
                "Perez",
                "roberto@example.com",
                null
        );

        when(customerRepository.findByUserId("user-123"))
                .thenReturn(Optional.empty());

        assertThrows(
                CustomerNotFoundException.class,
                () -> updateCustomerUseCase.execute(customer)
        );

        verify(customerRepository, never())
                .save(any(Customer.class));
    }
}