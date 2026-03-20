package com.example.springbank.controller.dto;

import jakarta.validation.constraints.NotBlank;

public class RequestEmployerCreateBody {
    @NotBlank
    private String company;
    @NotBlank
    private String address;

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
