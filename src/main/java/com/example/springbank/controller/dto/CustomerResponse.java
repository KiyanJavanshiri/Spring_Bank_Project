package com.example.springbank.controller.dto;

import com.example.springbank.model.Account;
import com.example.springbank.model.Employer;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CustomerResponse {
    private Long id;
    private String name;
    private String email;
    private String phoneNumber;
    private int age;
    private List<AccountResponse> accounts;
    private Set<EmployerResponse> employers;
}
