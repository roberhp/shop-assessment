package com.liverpool.appsales.exam.customer.application;

import com.liverpool.appsales.exam.customer.application.exception.CustomerNotFoundException;
import com.liverpool.appsales.exam.customer.application.exception.InvalidCustomerOrdersException;
import com.liverpool.appsales.exam.customer.domain.Customer;
import com.liverpool.appsales.exam.order.application.OrderRepository;
import com.liverpool.appsales.exam.order.domain.Order;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UpdateCustomerUseCaseTest {

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private OrderRepository orderRepository;

    private UpdateCustomerUseCase updateCustomerUseCase;

    @BeforeEach
    void setUp() {
        updateCustomerUseCase =
                new UpdateCustomerUseCase(
                        customerRepository,
                        orderRepository);
    }

    @Test
    void shouldUpdateCustomerAndPreserveOrdersWhenOrdersAreNull() {

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

        verify(orderRepository, never())
                .findByUserId(any());
    }

    @Test
    void shouldUpdateCustomerAndAssociateOrders() {

        Customer existingCustomer = new Customer(
                "user-123",
                "Roberto",
                "Huerta",
                "Perez",
                "old@example.com",
                List.of()
        );

        Customer customerToUpdate = new Customer(
                "user-123",
                "Roberto",
                "NuevoApellido",
                "NuevoMaterno",
                "new@example.com",
                List.of(
                        "3010091676",
                        "30100916760987"
                )
        );

        Order firstOrder = createOrder(
                "3010091676",
                "user-123");

        Order secondOrder = createOrder(
                "30100916760987",
                "user-123");

        when(customerRepository.findByUserId("user-123"))
                .thenReturn(Optional.of(existingCustomer));

        when(orderRepository.findByUserId("user-123"))
                .thenReturn(List.of(firstOrder, secondOrder));

        when(customerRepository.save(any(Customer.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        Customer result =
                updateCustomerUseCase.execute(customerToUpdate);

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

        assertEquals(
                List.of(
                        "3010091676",
                        "30100916760987"
                ),
                captor.getValue().getOrders()
        );
    }

    @Test
    void shouldClearOrdersWhenEmptyListIsProvided() {

        Customer existingCustomer = new Customer(
                "user-123",
                "Roberto",
                "Huerta",
                "Perez",
                "old@example.com",
                List.of(
                        "3010091676"
                )
        );

        Customer customerToUpdate = new Customer(
                "user-123",
                "Roberto",
                "Huerta",
                "Perez",
                "new@example.com",
                List.of()
        );

        when(customerRepository.findByUserId("user-123"))
                .thenReturn(Optional.of(existingCustomer));

        when(orderRepository.findByUserId("user-123"))
                .thenReturn(List.of());

        when(customerRepository.save(any(Customer.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        Customer result =
                updateCustomerUseCase.execute(customerToUpdate);

        assertEquals(List.of(), result.getOrders());
    }

    @Test
    void shouldRejectOrderThatDoesNotBelongToCustomer() {

        Customer existingCustomer = new Customer(
                "user-123",
                "Roberto",
                "Huerta",
                "Perez",
                "old@example.com",
                List.of()
        );

        Customer customerToUpdate = new Customer(
                "user-123",
                "Roberto",
                "Huerta",
                "Perez",
                "new@example.com",
                List.of("order-from-another-user")
        );

        Order customerOrder = createOrder(
                "3010091676",
                "user-123");

        when(customerRepository.findByUserId("user-123"))
                .thenReturn(Optional.of(existingCustomer));

        when(orderRepository.findByUserId("user-123"))
                .thenReturn(List.of(customerOrder));

        assertThrows(
                InvalidCustomerOrdersException.class,
                () -> updateCustomerUseCase.execute(customerToUpdate)
        );

        verify(customerRepository, never())
                .save(any(Customer.class));
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

    private Order createOrder(
            String orderRef,
            String userId) {

        return new Order(
                orderRef,
                userId,
                "online",
                "2025-12-06",
                "L SANTA FE",
                LocalDate.of(2025, 12, 6),
                List.of()
        );
    }
}