package com.example.springbank.controller.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RequestAccountTransactionBody {
    @NotBlank
    private String number;
    @Positive
    private double sum;
}
