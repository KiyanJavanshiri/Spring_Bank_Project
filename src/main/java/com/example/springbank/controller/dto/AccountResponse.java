package com.example.springbank.controller.dto;

import com.example.springbank.model.Currency;
import com.example.springbank.model.Customer;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AccountResponse {
    private Long id;
    private String number;
    private Double balance;
    private Currency currency;
}
