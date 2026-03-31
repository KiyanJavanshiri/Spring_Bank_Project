package com.example.springbank.controller.dto;

import com.example.springbank.model.Customer;
import lombok.*;

import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EmployerResponse {
    private Long id;
    private String name;
    private String address;
}
