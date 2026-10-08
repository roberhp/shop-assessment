package com.liverpool.appsales.exam.customer.application;

import com.liverpool.appsales.exam.customer.application.exception.CustomerNotFoundException;
import com.liverpool.appsales.exam.customer.domain.Customer;
import org.springframework.stereotype.Service;

@Service
public class GetCustomerUseCase {

    private final CustomerRepository customerRepository;

    public GetCustomerUseCase(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    public Customer execute(String userId) {

        return customerRepository.findByUserId(userId)
                .orElseThrow(() ->
                        new CustomerNotFoundException(userId)
                );
    }
}