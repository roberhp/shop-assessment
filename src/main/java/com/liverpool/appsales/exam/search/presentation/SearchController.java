package com.liverpool.appsales.exam.search.presentation;

import com.liverpool.appsales.exam.search.application.SearchUseCase;
import com.liverpool.appsales.exam.search.domain.SearchCriteria;
import com.liverpool.appsales.exam.search.domain.SearchResult;
import com.liverpool.appsales.exam.search.presentation.dto.SearchResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Search", description = "Order and item search operations")
@RestController
@RequestMapping("/search")
public class SearchController {

        private final SearchUseCase searchUseCase;

        public SearchController(SearchUseCase searchUseCase) {
                this.searchUseCase = searchUseCase;
        }

        @Operation(summary = "Search orders and items", 
                    description = "Searches orders using order reference, order status or store name, "
                        + "and searches items by display name using flexible text matching.")
        @ApiResponses({
                        @ApiResponse(responseCode = "200", 
                                description = "Search completed successfully", 
                                content = @Content(schema = @Schema(implementation = SearchResponse.class))),
                        @ApiResponse(responseCode = "400", 
                        description = "Invalid search parameters")
        })
        @GetMapping
        public ResponseEntity<SearchResponse> search(
                        @Parameter(description = "Order reference to search for", 
                                example = "3010091676") 
                        @RequestParam(required = false) String orderRef,
                        @Parameter(description = "Order status to filter orders", 
                                example = "DELIVERED") 
                        @RequestParam(required = false) String orderStatus,
                        @Parameter(description = "Store name to filter orders", 
                                example = "Liverpool Galerías") 
                        @RequestParam(required = false) String storeName,
                        @Parameter(description = "Item display name used for flexible text search", 
                                example = "Pantalón Levi's") 
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