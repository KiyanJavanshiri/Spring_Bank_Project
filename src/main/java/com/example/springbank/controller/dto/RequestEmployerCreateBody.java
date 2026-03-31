package com.example.springbank.controller.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RequestEmployerCreateBody {
    @Size(min = 3)
    private String name;
    @Size(min = 3)
    private String address;
}
