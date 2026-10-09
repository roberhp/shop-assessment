package com.liverpool.appsales.exam.delivery.presentation;

import com.liverpool.appsales.exam.delivery.application.CreateDeliveryUseCase;
import com.liverpool.appsales.exam.delivery.application.DeleteDeliveryUseCase;
import com.liverpool.appsales.exam.delivery.application.GetDeliveryUseCase;
import com.liverpool.appsales.exam.delivery.application.UpdateDeliveryUseCase;
import com.liverpool.appsales.exam.delivery.domain.Delivery;
import com.liverpool.appsales.exam.delivery.presentation.dto.CreateDeliveryRequest;
import com.liverpool.appsales.exam.delivery.presentation.dto.DeliveryResponse;
import com.liverpool.appsales.exam.delivery.presentation.dto.UpdateDeliveryRequest;
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
        name = "Entregas",
        description = "Operaciones para la gestión de información de entrega"
)
@RestController
@RequestMapping("/deliveries")
public class DeliveryController {

    private final CreateDeliveryUseCase createDeliveryUseCase;

    private final GetDeliveryUseCase getDeliveryUseCase;

    private final UpdateDeliveryUseCase updateDeliveryUseCase;

    private final DeleteDeliveryUseCase deleteDeliveryUseCase;

    public DeliveryController(
            CreateDeliveryUseCase createDeliveryUseCase,
            GetDeliveryUseCase getDeliveryUseCase,
            UpdateDeliveryUseCase updateDeliveryUseCase,
            DeleteDeliveryUseCase deleteDeliveryUseCase) {
        this.createDeliveryUseCase = createDeliveryUseCase;
        this.getDeliveryUseCase = getDeliveryUseCase;
        this.updateDeliveryUseCase = updateDeliveryUseCase;
        this.deleteDeliveryUseCase = deleteDeliveryUseCase;
    }

    @Operation(
            summary = "Crear información de entrega",
            description = "Crea información de entrega asociada a un pedido."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "Información de entrega creada correctamente",
                    content = @Content(
                            schema = @Schema(implementation = DeliveryResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "La solicitud contiene datos inválidos"
            )
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

    @Operation(
            summary = "Consultar información de entrega",
            description = "Consulta la información de entrega utilizando su identificador."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Información de entrega encontrada",
                    content = @Content(
                            schema = @Schema(implementation = DeliveryResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Información de entrega no encontrada"
            )
    })
    @GetMapping("/{deliveryId}")
    public ResponseEntity<DeliveryResponse> get(
            @Parameter(
                    description = "Identificador único de la información de entrega",
                    required = true,
                    example = "delivery-001"
            )
            @PathVariable String deliveryId) {

        Delivery delivery = getDeliveryUseCase.execute(deliveryId);

        return ResponseEntity.ok(toResponse(delivery));
    }

    @Operation(
            summary = "Actualizar información de entrega",
            description = "Actualiza la información de entrega existente."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Información de entrega actualizada correctamente",
                    content = @Content(
                            schema = @Schema(implementation = DeliveryResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "La solicitud contiene datos inválidos"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Información de entrega no encontrada"
            )
    })
    @PutMapping("/{deliveryId}")
    public ResponseEntity<DeliveryResponse> update(
            @Parameter(
                    description = "Identificador único de la información de entrega",
                    required = true,
                    example = "delivery-001"
            )
            @PathVariable String deliveryId,
            @Valid @RequestBody UpdateDeliveryRequest request) {

        Delivery delivery = new Delivery(
                deliveryId,
                request.orderRef(),
                request.shippingAddress());

        Delivery updated = updateDeliveryUseCase.execute(delivery);

        return ResponseEntity.ok(toResponse(updated));
    }

        @DeleteMapping("/{deliveryId}")
        @Operation(
                summary = "Eliminar entrega",
                description = "Elimina una entrega utilizando su identificador."
        )
        @ApiResponse(
                responseCode = "204",
                description = "Entrega eliminada correctamente"
        )
        @ApiResponse(
                responseCode = "404",
                description = "Entrega no encontrada"
        )
        public ResponseEntity<Void> deleteDelivery(
                @PathVariable String deliveryId) {

        deleteDeliveryUseCase.execute(deliveryId);

        return ResponseEntity.noContent().build();
        }

    private DeliveryResponse toResponse(Delivery delivery) {

        return new DeliveryResponse(
                delivery.getDeliveryId(),
                delivery.getOrderRef(),
                delivery.getShippingAddress());
    }
}