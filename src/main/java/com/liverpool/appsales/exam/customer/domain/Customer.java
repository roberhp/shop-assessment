package com.liverpool.appsales.exam.customer.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Customer {

    private String userId;
    private String firstName;
    private String paternalLastName;
    private String maternalLastName;
    private String email;
    private List<String> orders;
}