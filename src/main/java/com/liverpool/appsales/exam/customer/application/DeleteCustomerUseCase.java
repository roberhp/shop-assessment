package com.liverpool.appsales.exam.customer.application;

import com.liverpool.appsales.exam.customer.application.exception.CustomerNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DeleteCustomerUseCase {

    private final CustomerRepository customerRepository;

    public void execute(String userId) {

        if (!customerRepository.existsByUserId(userId)) {
            throw new CustomerNotFoundException(
                    "No existe el cliente: " + userId
            );
        }

        customerRepository.deleteByUserId(userId);
    }
}