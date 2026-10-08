package com.liverpool.appsales.exam.customer.infrastructure;

import com.liverpool.appsales.exam.customer.application.CustomerRepository;
import com.liverpool.appsales.exam.customer.domain.Customer;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class CustomerRepositoryAdapter implements CustomerRepository {

    private final CustomerMongoRepository mongoRepository;

    public CustomerRepositoryAdapter(CustomerMongoRepository mongoRepository) {
        this.mongoRepository = mongoRepository;
    }

    @Override
    public Customer save(Customer customer) {

        CustomerDocument document = mongoRepository
                .findByUserId(customer.getUserId())
                .orElseGet(CustomerDocument::new);

        document.setUserId(customer.getUserId());
        document.setFirstName(customer.getFirstName());
        document.setPaternalLastName(customer.getPaternalLastName());
        document.setMaternalLastName(customer.getMaternalLastName());
        document.setEmail(customer.getEmail());
        document.setOrders(customer.getOrders());

        CustomerDocument savedDocument = mongoRepository.save(document);

        return toDomain(savedDocument);
    }

    @Override
    public Optional<Customer> findByUserId(String userId) {

        return mongoRepository.findByUserId(userId)
                .map(this::toDomain);
    }

    @Override
    public boolean existsByUserId(String userId) {

        return mongoRepository.existsByUserId(userId);
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