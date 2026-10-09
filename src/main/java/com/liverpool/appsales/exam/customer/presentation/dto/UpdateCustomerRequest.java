package com.liverpool.appsales.exam.customer.presentation.dto;

import java.util.List;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record UpdateCustomerRequest(
        @NotBlank String firstName,
        @NotBlank String paternalLastName,
        @NotBlank String maternalLastName,
        @NotBlank @Email String email,
        List<String> orders) {
}