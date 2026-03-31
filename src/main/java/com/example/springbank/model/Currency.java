package com.example.springbank.model;

import lombok.Getter;

@Getter
public enum Currency {
    USD("Dollars"),
    EUR("Euro"),
    UAH("Hryvna"),
    CHF("Franc"),
    GBP("Pound");

    private final String currency;

    Currency(String currency) {
        this.currency = currency;
    }
}
