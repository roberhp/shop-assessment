package com.liverpool.appsales.exam.customer.presentation;

import com.liverpool.appsales.exam.customer.application.CreateCustomerUseCase;
import com.liverpool.appsales.exam.customer.application.GetCustomerUseCase;
import com.liverpool.appsales.exam.customer.application.UpdateCustomerUseCase;
import com.liverpool.appsales.exam.customer.domain.Customer;
import com.liverpool.appsales.exam.customer.presentation.dto.CreateCustomerRequest;
import com.liverpool.appsales.exam.customer.presentation.dto.CustomerResponse;
import com.liverpool.appsales.exam.customer.presentation.dto.UpdateCustomerRequest;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/customers")
public class CustomerController {

    private final CreateCustomerUseCase createCustomerUseCase;
    private final GetCustomerUseCase getCustomerUseCase;
    private final UpdateCustomerUseCase updateCustomerUseCase;

    public CustomerController(
            CreateCustomerUseCase createCustomerUseCase,
            GetCustomerUseCase getCustomerUseCase,
            UpdateCustomerUseCase updateCustomerUseCase
    ) {
        this.createCustomerUseCase = createCustomerUseCase;
        this.getCustomerUseCase = getCustomerUseCase;
        this.updateCustomerUseCase = updateCustomerUseCase;
    }

    @PostMapping
    public ResponseEntity<CustomerResponse> create(
            @Valid @RequestBody CreateCustomerRequest request
    ) {

        Customer customer = new Customer(
                request.userId(),
                request.firstName(),
                request.paternalLastName(),
                request.maternalLastName(),
                request.email(),
                null
        );

        Customer created = createCustomerUseCase.execute(customer);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(toResponse(created));
    }

    @GetMapping("/{userId}")
    public ResponseEntity<CustomerResponse> get(
            @PathVariable String userId
    ) {

        Customer customer = getCustomerUseCase.execute(userId);

        return ResponseEntity.ok(toResponse(customer));
    }

    @PutMapping("/{userId}")
    public ResponseEntity<CustomerResponse> update(
            @PathVariable String userId,
            @Valid @RequestBody UpdateCustomerRequest request) {

        Customer customer = new Customer(
                userId,
                request.firstName(),
                request.paternalLastName(),
                request.maternalLastName(),
                request.email(),
                null
        );

        Customer updated = updateCustomerUseCase.execute(customer);

        return ResponseEntity.ok(toResponse(updated));
    }

    private CustomerResponse toResponse(Customer customer) {

        return new CustomerResponse(
                customer.getUserId(),
                customer.getFirstName(),
                customer.getPaternalLastName(),
                customer.getMaternalLastName(),
                customer.getEmail(),
                customer.getOrders()
        );
    }
}