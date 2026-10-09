package com.liverpool.appsales.exam.customer.presentation.dto;

import java.util.List;

public record CustomerResponse(
    String userId,
    String firstName,
    String paternalLastName,
    String maternalLastName,
    String email,
    List<String> orders
) {
}