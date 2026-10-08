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
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/deliveries")
public class DeliveryController {

    private final CreateDeliveryUseCase createDeliveryUseCase;
    private final GetDeliveryUseCase getDeliveryUseCase;
    private final UpdateDeliveryUseCase updateDeliveryUseCase;

    public DeliveryController(
            CreateDeliveryUseCase createDeliveryUseCase,
            GetDeliveryUseCase getDeliveryUseCase,
            UpdateDeliveryUseCase updateDeliveryUseCase
    ) {
        this.createDeliveryUseCase = createDeliveryUseCase;
        this.getDeliveryUseCase = getDeliveryUseCase;
        this.updateDeliveryUseCase = updateDeliveryUseCase;
    }

    @PostMapping
    public ResponseEntity<DeliveryResponse> create(
            @Valid @RequestBody CreateDeliveryRequest request
    ) {

        Delivery delivery = new Delivery(
                null,
                request.orderRef(),
                request.shippingAddress()
        );

        Delivery created = createDeliveryUseCase.execute(delivery);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(toResponse(created));
    }

    @GetMapping("/{deliveryId}")
    public ResponseEntity<DeliveryResponse> get(
            @PathVariable String deliveryId
    ) {

        Delivery delivery = getDeliveryUseCase.execute(deliveryId);

        return ResponseEntity.ok(toResponse(delivery));
    }

    @PutMapping("/{deliveryId}")
    public ResponseEntity<DeliveryResponse> update(
            @PathVariable String deliveryId,
            @Valid @RequestBody UpdateDeliveryRequest request
    ) {

        Delivery delivery = new Delivery(
                deliveryId,
                request.orderRef(),
                request.shippingAddress()
        );

        Delivery updated = updateDeliveryUseCase.execute(delivery);

        return ResponseEntity.ok(toResponse(updated));
    }

    private DeliveryResponse toResponse(Delivery delivery) {

        return new DeliveryResponse(
                delivery.getDeliveryId(),
                delivery.getOrderRef(),
                delivery.getShippingAddress()
        );
    }
}