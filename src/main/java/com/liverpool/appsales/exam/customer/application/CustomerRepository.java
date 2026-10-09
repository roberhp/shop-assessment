package com.liverpool.appsales.exam.customer.application;

import com.liverpool.appsales.exam.customer.domain.Customer;

import java.util.Optional;

public interface CustomerRepository {

    Customer save(Customer customer);

    Optional<Customer> findByUserId(String userId);

    boolean existsByUserId(String userId);

    void deleteByUserId(String userId);
}