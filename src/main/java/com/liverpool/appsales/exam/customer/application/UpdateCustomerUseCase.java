package com.liverpool.appsales.exam.customer.application;

import com.liverpool.appsales.exam.customer.application.exception.CustomerNotFoundException;
import com.liverpool.appsales.exam.customer.application.exception.InvalidCustomerOrdersException;
import com.liverpool.appsales.exam.customer.domain.Customer;
import com.liverpool.appsales.exam.order.application.OrderRepository;
import com.liverpool.appsales.exam.order.domain.Order;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UpdateCustomerUseCase {

    private final CustomerRepository customerRepository;
    private final OrderRepository orderRepository;

    public UpdateCustomerUseCase(
            CustomerRepository customerRepository,
            OrderRepository orderRepository) {

        this.customerRepository = customerRepository;
        this.orderRepository = orderRepository;
    }

    public Customer execute(Customer customer) {

        Customer existingCustomer = customerRepository
                .findByUserId(customer.getUserId())
                .orElseThrow(() ->
                        new CustomerNotFoundException(customer.getUserId())
                );

        existingCustomer.setFirstName(customer.getFirstName());
        existingCustomer.setPaternalLastName(customer.getPaternalLastName());
        existingCustomer.setMaternalLastName(customer.getMaternalLastName());
        existingCustomer.setEmail(customer.getEmail());

        if (customer.getOrders() != null) {
            validateOrdersBelongToCustomer(
                    customer.getUserId(),
                    customer.getOrders()
            );

            existingCustomer.setOrders(customer.getOrders());
        }

        return customerRepository.save(existingCustomer);
    }

    private void validateOrdersBelongToCustomer(
            String userId,
            List<String> orderRefs) {

        List<Order> customerOrders =
                orderRepository.findByUserId(userId);

        Set<String> validOrderRefs = customerOrders.stream()
                .map(Order::getOrderRef)
                .collect(Collectors.toSet());

        Set<String> invalidOrderRefs = new HashSet<>(orderRefs);
        invalidOrderRefs.removeAll(validOrderRefs);

        if (!invalidOrderRefs.isEmpty()) {
            throw new InvalidCustomerOrdersException(
                    "Los siguientes pedidos no pertenecen al cliente "
                            + userId + ": "
                            + invalidOrderRefs
            );
        }
    }
}