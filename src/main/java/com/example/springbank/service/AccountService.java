package com.example.springbank.service;

import com.example.springbank.controller.dto.AccountResponse;
import com.example.springbank.mapper.AccountMapper;
import com.example.springbank.model.Account;
import com.example.springbank.repository.AccountRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class AccountService {
    private final AccountRepository accountRepository;
    private final AccountWebSocketService accountWebSocketService;
    private AccountMapper accountMapper;

    public AccountResponse getAccount(long id) {
        Account account = accountRepository.findById(id).orElse(null);
        return account == null ? null : accountMapper.toResponse(account);
    }

    public List<AccountResponse> getAllAccounts() {
        return accountRepository.findAll().stream().map(accountMapper::toResponse).collect(Collectors.toList());
    }

    public AccountResponse deposit(String number, double sum) {
        Account account = accountRepository.findByNumber(number).orElse(null);

        if(account == null) {
            return null;
        }

        account.setBalance(account.getBalance() + sum);
        accountRepository.save(account);

        AccountResponse response = accountMapper.toResponse(account);

        accountWebSocketService.sendBalanceUpdate(
                account.getCustomer().getId(),
                "Balance updated: " + response.getNumber() + " = " + response.getBalance()
        );
        return response;
    }

    public AccountResponse withdrawal(String number, double sum) {
        Account account = accountRepository.findByNumber(number).orElse(null);

        if(account == null || account.getBalance() < sum) {
            return null;
        }

        account.setBalance(account.getBalance() - sum);
        accountRepository.save(account);

        AccountResponse response = accountMapper.toResponse(account);

        accountWebSocketService.sendBalanceUpdate(
                account.getCustomer().getId(),
                "Balance updated: " + response.getNumber() + " = " + response.getBalance()
        );
        return response;
    }

    public boolean transfer(String from, String to, double sum) {
        Account fromAcc = accountRepository.findByNumber(from).orElse(null);
        Account toAcc = accountRepository.findByNumber(to).orElse(null);

        if (fromAcc == null || toAcc == null) return false;
        if (fromAcc.getBalance() < sum) return false;

        fromAcc.setBalance(fromAcc.getBalance() - sum);
        toAcc.setBalance(toAcc.getBalance() + sum);

        accountRepository.save(fromAcc);
        accountRepository.save(toAcc);

        accountWebSocketService.sendBalanceUpdate(
                fromAcc.getCustomer().getId(),
                "TRANSFER OUT: -" + sum
        );

        accountWebSocketService.sendBalanceUpdate(
                toAcc.getCustomer().getId(),
                "TRANSFER IN: +" + sum
        );

        return true;
    }

    public void saveAccount(Account account) {
        accountRepository.save(account);
    }
}
