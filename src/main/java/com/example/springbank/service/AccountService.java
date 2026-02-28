package com.example.springbank.service;

import com.example.springbank.model.Account;
import com.example.springbank.model.Currency;
import com.example.springbank.model.Customer;
import com.example.springbank.repository.AccountRepository;
import com.example.springbank.repository.CustomerRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AccountService {
    private final AccountRepository accountRepository;
    private final CustomerRepository customerRepository;

    public AccountService(AccountRepository accountRepository, CustomerRepository customerRepository) {
        this.accountRepository = accountRepository;
        this.customerRepository = customerRepository;
    }

//    public Account createAccount(long customerId, Currency currency) {
//        Customer customer = customerRepository.findById(customerId).orElse(null);
//
//        if(customer == null) return null;
//
//        Account newAccount = new Account(currency, customer);
//
//        customer.addAccount(newAccount);
//
//        return newAccount;
//    }
}
