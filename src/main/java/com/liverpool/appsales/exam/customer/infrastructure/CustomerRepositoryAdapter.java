package com.liverpool.appsales.exam.customer.infrastructure;

import com.liverpool.appsales.exam.customer.application.CustomerRepository;
import com.liverpool.appsales.exam.customer.domain.Customer;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class CustomerRepositoryAdapter implements CustomerRepository {

    private final CustomerMongoRepository repository;

    @Override
    public Customer save(Customer customer) {
        return toDomain(repository.save(toDocument(customer)));
    }

    @Override
    public Optional<Customer> findByUserId(String userId) {
        return repository.findByUserId(userId)
                .map(this::toDomain);
    }

    @Override
    public boolean existsByUserId(String userId) {
        return repository.existsByUserId(userId);
    }

    @Override
    public void deleteByUserId(String userId) {
        repository.deleteByUserId(userId);
    }

    private CustomerDocument toDocument(Customer customer) {
        CustomerDocument document = new CustomerDocument();

        document.setId(customer.getUserId());
        document.setUserId(customer.getUserId());
        document.setFirstName(customer.getFirstName());
        document.setPaternalLastName(customer.getPaternalLastName());
        document.setMaternalLastName(customer.getMaternalLastName());
        document.setEmail(customer.getEmail());
        document.setOrders(customer.getOrders());

        return document;
    }

    private Customer toDomain(CustomerDocument document) {
        return new Customer(
                document.getUserId(),
                document.getFirstName(),
                document.getPaternalLastName(),
                document.getMaternalLastName(),
                document.getEmail(),
                document.getOrders()
        );
    }
}