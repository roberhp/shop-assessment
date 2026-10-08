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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Customers", description = "Operations for customer management")
@RestController
@RequestMapping("/customers")
public class CustomerController {

        private final CreateCustomerUseCase createCustomerUseCase;

        private final GetCustomerUseCase getCustomerUseCase;

        private final UpdateCustomerUseCase updateCustomerUseCase;

        public CustomerController(
                        CreateCustomerUseCase createCustomerUseCase,
                        GetCustomerUseCase getCustomerUseCase,
                        UpdateCustomerUseCase updateCustomerUseCase) {
                this.createCustomerUseCase = createCustomerUseCase;
                this.getCustomerUseCase = getCustomerUseCase;
                this.updateCustomerUseCase = updateCustomerUseCase;
        }

        @Operation(summary = "Create a customer", description = "Creates a new customer.")
        @ApiResponses({
                        @ApiResponse(responseCode = "201", 
                        description = "Customer created successfully", 
                        content = @Content(schema = @Schema(implementation = CustomerResponse.class))),
                        @ApiResponse(responseCode = "400", 
                        description = "Invalid request"),
                        @ApiResponse(responseCode = "409", 
                        description = "Customer already exists")
        })
        @PostMapping
        public ResponseEntity<CustomerResponse> create(
                        @Valid @RequestBody CreateCustomerRequest request) {

                Customer customer = new Customer(
                                request.userId(),
                                request.firstName(),
                                request.paternalLastName(),
                                request.maternalLastName(),
                                request.email(),
                                null);

                Customer created = createCustomerUseCase.execute(customer);

                return ResponseEntity
                                .status(HttpStatus.CREATED)
                                .body(toResponse(created));
        }

        @Operation(summary = "Get a customer", description = "Retrieves a customer using its user identifier.")
        @ApiResponses({
                        @ApiResponse(responseCode = "200", 
                                description = "Customer found", 
                                content = @Content(schema = @Schema(implementation = CustomerResponse.class))),
                        @ApiResponse(responseCode = "404", 
                                description = "Customer not found")
        })
        @GetMapping("/{userId}")
        public ResponseEntity<CustomerResponse> get(
                        @PathVariable String userId) {

                Customer customer = getCustomerUseCase.execute(userId);

                return ResponseEntity.ok(toResponse(customer));
        }

        @PutMapping("/{userId}")
        public ResponseEntity<CustomerResponse> update(
                        @Parameter(
                                description = "Unique identifier of the customer",
                                required = true,
                                example = "75c97531-abf5-4524-8107-90aa48d08efc"
                        ) 
                        @PathVariable String userId,
                        @Valid @RequestBody UpdateCustomerRequest request) {

                Customer customer = new Customer(
                                userId,
                                request.firstName(),
                                request.paternalLastName(),
                                request.maternalLastName(),
                                request.email(),
                                null);

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
                                customer.getOrders());
        }
}