package com.example.springbank.controller.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

public class RequestAccountTransferBody {
    @NotBlank
    private String firstCardNumber;
    @NotBlank
    private String secondCardNumber;
    @Positive
    private double sum;

    public double getSum() {
        return sum;
    }

    public void setSum(double sum) {
        this.sum = sum;
    }

    public String getFirstCardNumber() {
        return firstCardNumber;
    }

    public void setFirstCardNumber(String firstCardNumber) {
        this.firstCardNumber = firstCardNumber;
    }

    public String getSecondCardNumber() {
        return secondCardNumber;
    }

    public void setSecondCardNumber(String secondCardNumber) {
        this.secondCardNumber = secondCardNumber;
    }
}
