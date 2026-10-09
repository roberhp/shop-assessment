package com.liverpool.appsales.exam.customer.infrastructure;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "customers")
public class CustomerDocument {

    @Id
    private String id;

    private String userId;

    private String firstName;

    private String paternalLastName;

    private String maternalLastName;

    private String email;

    private List<String> orders;
}