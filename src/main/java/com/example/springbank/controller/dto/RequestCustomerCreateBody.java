package com.example.springbank.controller.dto;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RequestCustomerCreateBody {
    @Size(min = 2)
    private String name;

    @Email
    private String email;

    @Pattern(regexp = "\\+?\\d{10,15}")
    private String phoneNumber;

    @Positive
    @Min(18)
    private int age;

    private String password;
}
