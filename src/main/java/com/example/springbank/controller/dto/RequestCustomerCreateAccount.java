package com.example.springbank.controller.dto;

import com.example.springbank.model.Currency;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RequestCustomerCreateAccount {
    private Currency currency;
}
