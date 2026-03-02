package com.example.springbank.controller;

import com.example.springbank.controller.dto.RequestAccountTransactionBody;
import com.example.springbank.controller.dto.RequestAccountTransferBody;
import com.example.springbank.model.Account;
import com.example.springbank.service.AccountService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/accounts")
public class AccountController {
    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @GetMapping
    public ResponseEntity<List<Account>> getAccounts() {
        return ResponseEntity.ok(accountService.getAllAccounts());
    }

    @PostMapping("/deposit")
    public ResponseEntity<Account> depositAccount(@Valid @RequestBody RequestAccountTransactionBody body) {
        Account account = accountService.deposit(body.getNumber(), body.getSum());

        if (account == null) {
            return ResponseEntity.badRequest().build();
        }

        accountService.saveAccount(account);

        return ResponseEntity.status(201).body(account);
    }

    @PostMapping("/withdrawal")
    public ResponseEntity<Account> withdrawalAccount(@Valid @RequestBody RequestAccountTransactionBody body) {
        Account account = accountService.withdrawal(body.getNumber(), body.getSum());

        if (account == null) {
            return ResponseEntity.badRequest().build();
        }

        accountService.saveAccount(account);

        return ResponseEntity.status(201).body(account);
    }

    @PostMapping("/transfer")
    public ResponseEntity<Void> transfer(@Valid @RequestBody RequestAccountTransferBody body) {
        Account withdrawalAccount = accountService.withdrawal(body.getFirstCardNumber(), body.getSum());

        if(withdrawalAccount == null) {
            return ResponseEntity.badRequest().build();
        }

        Account depositAccount = accountService.deposit(body.getSecondCardNumber(), body.getSum());

        accountService.saveAccount(withdrawalAccount);
        accountService.saveAccount(depositAccount);

        return ResponseEntity.noContent().build();
    }
}
