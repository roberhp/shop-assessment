package com.liverpool.appsales.exam.search.presentation;

import com.liverpool.appsales.exam.search.application.SearchUseCase;
import com.liverpool.appsales.exam.search.domain.SearchCriteria;
import com.liverpool.appsales.exam.search.domain.SearchResult;
import com.liverpool.appsales.exam.search.presentation.dto.SearchResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(
        name = "Búsqueda",
        description = "Búsqueda flexible de pedidos y productos"
)
@RestController
@RequestMapping("/search")
public class SearchController {

    private final SearchUseCase searchUseCase;

    public SearchController(SearchUseCase searchUseCase) {
        this.searchUseCase = searchUseCase;
    }

    @Operation(
            summary = "Buscar pedidos y productos",
            description = "Permite buscar pedidos mediante número de pedido, "
                    + "estatus de pedido o tienda, y productos mediante su nombre. "
                    + "La búsqueda de texto es flexible y permite ignorar "
                    + "mayúsculas, acentos, signos de puntuación y pequeños "
                    + "errores de escritura."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Búsqueda realizada correctamente",
                    content = @Content(
                            schema = @Schema(implementation = SearchResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Los parámetros de búsqueda no son válidos"
            )
    })
    @GetMapping
    public ResponseEntity<SearchResponse> search(
            @Parameter(
                    description = "Número de pedido que se desea buscar",
                    example = "3010091676"
            )
            @RequestParam(required = false) String orderRef,

            @Parameter(
                    description = "Estatus del pedido que se desea buscar",
                    example = "2025-12-06"
            )
            @RequestParam(required = false) String orderStatus,

            @Parameter(
                    description = "Nombre de la tienda que se desea buscar",
                    example = "Liverpool Galerías"
            )
            @RequestParam(required = false) String storeName,

            @Parameter(
                    description = "Nombre del producto que se desea buscar",
                    example = "Pantalón Levi's"
            )
            @RequestParam(required = false) String displayName) {

        SearchCriteria criteria = new SearchCriteria(
                orderRef,
                orderStatus,
                storeName,
                displayName);

        SearchResult result = searchUseCase.execute(criteria);

        return ResponseEntity.ok(
                new SearchResponse(
                        result.getOrders(),
                        result.getItems()));
    }
}