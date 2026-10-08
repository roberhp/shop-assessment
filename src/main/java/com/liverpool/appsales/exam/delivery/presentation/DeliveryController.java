package com.liverpool.appsales.exam.delivery.presentation;

import com.liverpool.appsales.exam.delivery.application.CreateDeliveryUseCase;
import com.liverpool.appsales.exam.delivery.application.GetDeliveryUseCase;
import com.liverpool.appsales.exam.delivery.application.UpdateDeliveryUseCase;
import com.liverpool.appsales.exam.delivery.domain.Delivery;
import com.liverpool.appsales.exam.delivery.presentation.dto.CreateDeliveryRequest;
import com.liverpool.appsales.exam.delivery.presentation.dto.DeliveryResponse;
import com.liverpool.appsales.exam.delivery.presentation.dto.UpdateDeliveryRequest;
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

@Tag(name = "Deliveries", description = "Operations for delivery information management")
@RestController
@RequestMapping("/deliveries")
public class DeliveryController {

        private final CreateDeliveryUseCase createDeliveryUseCase;

        private final GetDeliveryUseCase getDeliveryUseCase;

        private final UpdateDeliveryUseCase updateDeliveryUseCase;

        public DeliveryController(
                        CreateDeliveryUseCase createDeliveryUseCase,
                        GetDeliveryUseCase getDeliveryUseCase,
                        UpdateDeliveryUseCase updateDeliveryUseCase) {
                this.createDeliveryUseCase = createDeliveryUseCase;
                this.getDeliveryUseCase = getDeliveryUseCase;
                this.updateDeliveryUseCase = updateDeliveryUseCase;
        }

        @Operation(summary = "Create delivery information", 
                description = "Creates delivery information associated with an order.")
        @ApiResponses({
                        @ApiResponse(responseCode = "201", 
                                description = "Delivery information created successfully", 
                                content = @Content(schema = @Schema(implementation = DeliveryResponse.class))),
                        @ApiResponse(responseCode = "400", 
                        description = "Invalid request")
        })
        @PostMapping
        public ResponseEntity<DeliveryResponse> create(
                        @Valid @RequestBody CreateDeliveryRequest request) {

                Delivery delivery = new Delivery(
                                null,
                                request.orderRef(),
                                request.shippingAddress());

                Delivery created = createDeliveryUseCase.execute(delivery);

                return ResponseEntity
                                .status(HttpStatus.CREATED)
                                .body(toResponse(created));
        }

        @Operation(summary = "Get delivery information", 
                description = "Retrieves delivery information using its delivery identifier.")
        @ApiResponses({
                        @ApiResponse(responseCode = "200", 
                                description = "Delivery information found", 
                                content = @Content(schema = @Schema(implementation = DeliveryResponse.class))),
                        @ApiResponse(responseCode = "404", 
                                description = "Delivery information not found")
        })
        @GetMapping("/{deliveryId}")
        public ResponseEntity<DeliveryResponse> get(
                        @Parameter(description = "Unique delivery identifier", 
                                  required = true, 
                                  example = "550e8400-e29b-41d4-a716-446655440000") @PathVariable String deliveryId) {

                Delivery delivery = getDeliveryUseCase.execute(deliveryId);

                return ResponseEntity.ok(toResponse(delivery));
        }

        @Operation(summary = "Update delivery information", 
                description = "Updates the delivery information of an existing delivery.")
        @ApiResponses({
                        @ApiResponse(responseCode = "200", 
                                description = "Delivery information updated successfully", 
                                content = @Content(schema = @Schema(implementation = DeliveryResponse.class))),
                        @ApiResponse(responseCode = "400", 
                                description = "Invalid request"),
                        @ApiResponse(responseCode = "404", 
                                description = "Delivery information not found")
        })
        @PutMapping("/{deliveryId}")
        public ResponseEntity<DeliveryResponse> update(
                        @PathVariable String deliveryId,
                        @Valid @RequestBody UpdateDeliveryRequest request) {

                Delivery delivery = new Delivery(
                                deliveryId,
                                request.orderRef(),
                                request.shippingAddress());

                Delivery updated = updateDeliveryUseCase.execute(delivery);

                return ResponseEntity.ok(toResponse(updated));
        }

        private DeliveryResponse toResponse(Delivery delivery) {

                return new DeliveryResponse(
                                delivery.getDeliveryId(),
                                delivery.getOrderRef(),
                                delivery.getShippingAddress());
        }
}