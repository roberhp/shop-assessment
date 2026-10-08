package com.liverpool.appsales.exam.customer.presentation.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record CreateCustomerRequest(

    @NotBlank
    String userId,

    @NotBlank
    String firstName,

    @NotBlank
    String paternalLastName,

    @NotBlank
    String maternalLastName,

    @NotBlank
    @Email
    String email
) {
}