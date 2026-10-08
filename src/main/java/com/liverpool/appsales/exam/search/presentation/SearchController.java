package com.liverpool.appsales.exam.search.presentation;

import com.liverpool.appsales.exam.search.application.SearchUseCase;
import com.liverpool.appsales.exam.search.domain.SearchCriteria;
import com.liverpool.appsales.exam.search.domain.SearchResult;
import com.liverpool.appsales.exam.search.presentation.dto.SearchResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/search")
public class SearchController {

    private final SearchUseCase searchUseCase;

    public SearchController(SearchUseCase searchUseCase) {
        this.searchUseCase = searchUseCase;
    }

    @GetMapping
    public ResponseEntity<SearchResponse> search(
            @RequestParam(required = false) String orderRef,
            @RequestParam(required = false) String orderStatus,
            @RequestParam(required = false) String storeName,
            @RequestParam(required = false) String displayName) {

        SearchCriteria criteria = new SearchCriteria(
                orderRef,
                orderStatus,
                storeName,
                displayName
        );

        SearchResult result = searchUseCase.execute(criteria);

        return ResponseEntity.ok(
                new SearchResponse(
                        result.getOrders(),
                        result.getItems()
                )
        );
    }
}