package com.example.springbank.controller.dto;

import com.example.springbank.model.Currency;
import lombok.Getter;

@Getter
public class RequestCustomerCreateAccount {
    private Currency currency;
}
