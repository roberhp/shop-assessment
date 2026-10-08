package com.liverpool.appsales.exam.customer.application;

import com.liverpool.appsales.exam.customer.application.exception.CustomerNotFoundException;
import com.liverpool.appsales.exam.customer.domain.Customer;
import org.springframework.stereotype.Service;

@Service
public class UpdateCustomerUseCase {

    private final CustomerRepository customerRepository;

    public UpdateCustomerUseCase(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
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

        return customerRepository.save(existingCustomer);
    }
}