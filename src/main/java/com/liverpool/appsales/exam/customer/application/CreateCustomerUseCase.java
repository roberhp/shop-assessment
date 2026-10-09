package com.liverpool.appsales.exam.customer.application;

import com.liverpool.appsales.exam.customer.application.exception.CustomerAlreadyExistsException;
import com.liverpool.appsales.exam.customer.domain.Customer;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class CreateCustomerUseCase {

    private final CustomerRepository customerRepository;

    public CreateCustomerUseCase(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    public Customer execute(Customer customer) {

        if (customerRepository.existsByUserId(customer.getUserId())) {
            throw new CustomerAlreadyExistsException(customer.getUserId());
        }

        if (customer.getOrders() == null) {
            customer.setOrders(new ArrayList<>());
        }

        return customerRepository.save(customer);
    }
}