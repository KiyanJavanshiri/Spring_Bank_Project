package com.example.springbank.service;

import com.example.springbank.model.Account;
import com.example.springbank.repository.AccountRepository;
import com.example.springbank.repository.CustomerRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AccountService {
    private final AccountRepository accountRepository;

    public AccountService(AccountRepository accountRepository, CustomerRepository customerRepository) {
        this.accountRepository = accountRepository;
    }

    public Account getAccount(long id) {
        return accountRepository.findById(id).orElse(null);
    }

    public List<Account> getAllAccounts() {
        return accountRepository.findAll();
    }

    public Account deposit(String number, double sum) {
        Account account = accountRepository.findByNumber(number).orElse(null);

        if(account == null) {
            return null;
        }

        account.setBalance(account.getBalance() + sum);

        return account;
    }

    public Account withdrawal(String number, double sum) {
        Account account = accountRepository.findByNumber(number).orElse(null);

        if(account == null || account.getBalance() < sum) {
            return null;
        }

        account.setBalance(account.getBalance() - sum);

        return account;
    }



    public void saveAccount(Account account) {
        accountRepository.save(account);
    }
}
