package com.liverpool.appsales.exam.search.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SearchCriteria {

    private String orderRef;
    private String orderStatus;
    private String storeName;
    private String displayName;
}