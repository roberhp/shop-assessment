package com.liverpool.appsales.exam.customer.presentation;

import com.liverpool.appsales.exam.customer.application.CreateCustomerUseCase;
import com.liverpool.appsales.exam.customer.application.DeleteCustomerUseCase;
import com.liverpool.appsales.exam.customer.application.GetCustomerUseCase;
import com.liverpool.appsales.exam.customer.application.UpdateCustomerUseCase;
import com.liverpool.appsales.exam.customer.domain.Customer;
import com.liverpool.appsales.exam.customer.presentation.dto.CreateCustomerRequest;
import com.liverpool.appsales.exam.customer.presentation.dto.CustomerResponse;
import com.liverpool.appsales.exam.customer.presentation.dto.UpdateCustomerRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(
        name = "Clientes",
        description = "Operaciones para la gestión de clientes"
)
@RestController
@RequestMapping("/customers")
public class CustomerController {

    private final CreateCustomerUseCase createCustomerUseCase;

    private final GetCustomerUseCase getCustomerUseCase;

    private final UpdateCustomerUseCase updateCustomerUseCase;

    private final DeleteCustomerUseCase deleteCustomerUseCase;

    public CustomerController(
            CreateCustomerUseCase createCustomerUseCase,
            GetCustomerUseCase getCustomerUseCase,
            UpdateCustomerUseCase updateCustomerUseCase,
            DeleteCustomerUseCase deleteCustomerUseCase) {
        this.createCustomerUseCase = createCustomerUseCase;
        this.getCustomerUseCase = getCustomerUseCase;
        this.updateCustomerUseCase = updateCustomerUseCase;
        this.deleteCustomerUseCase = deleteCustomerUseCase;
    }

    @Operation(
            summary = "Crear cliente",
            description = "Crea un nuevo cliente."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "Cliente creado correctamente",
                    content = @Content(
                            schema = @Schema(implementation = CustomerResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "La solicitud contiene datos inválidos"
            ),
            @ApiResponse(
                    responseCode = "409",
                    description = "El cliente ya existe"
            )
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

    @Operation(
            summary = "Consultar cliente",
            description = "Consulta un cliente utilizando su identificador de usuario."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Cliente encontrado",
                    content = @Content(
                            schema = @Schema(implementation = CustomerResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Cliente no encontrado"
            )
    })
    @GetMapping("/{userId}")
    public ResponseEntity<CustomerResponse> get(
            @Parameter(
                    description = "Identificador único del cliente",
                    required = true,
                    example = "75c97531-abf5-4524-8107-90aa48d08efc"
            )
            @PathVariable String userId) {

        Customer customer = getCustomerUseCase.execute(userId);

        return ResponseEntity.ok(toResponse(customer));
    }

    @Operation(
            summary = "Actualizar cliente",
            description = "Actualiza la información de un cliente existente."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Cliente actualizado correctamente",
                    content = @Content(
                            schema = @Schema(implementation = CustomerResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "La solicitud contiene datos inválidos"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Cliente no encontrado"
            )
    })
    @PutMapping("/{userId}")
    public ResponseEntity<CustomerResponse> update(
            @Parameter(
                    description = "Identificador único del cliente",
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

        @DeleteMapping("/{userId}")
        @Operation(
                summary = "Eliminar cliente",
                description = "Elimina un cliente utilizando su identificador de usuario."
        )
        @ApiResponse(
                responseCode = "204",
                description = "Cliente eliminado correctamente"
        )
        @ApiResponse(
                responseCode = "404",
                description = "Cliente no encontrado"
        )
        public ResponseEntity<Void> deleteCustomer(
                @PathVariable String userId) {

        deleteCustomerUseCase.execute(userId);

        return ResponseEntity.noContent().build();
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