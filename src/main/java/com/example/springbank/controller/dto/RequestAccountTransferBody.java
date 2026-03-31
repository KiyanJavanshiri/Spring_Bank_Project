package com.example.springbank.controller.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RequestAccountTransferBody {
    @NotBlank
    private String from;
    @NotBlank
    private String to;
    @Positive
    private double sum;
}
