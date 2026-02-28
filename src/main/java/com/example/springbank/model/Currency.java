package com.example.springbank.model;

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

    public String getCurrency() {
        return currency;
    }
}
