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

        return ResponseEntity.ok(account);
    }

    @PostMapping("/withdrawal")
    public ResponseEntity<Account> withdrawalAccount(@Valid @RequestBody RequestAccountTransactionBody body) {
        Account account = accountService.withdrawal(body.getNumber(), body.getSum());

        if (account == null) {
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.ok(account);
    }

    @PostMapping("/transfer")
    public ResponseEntity<Void> transfer(@Valid @RequestBody RequestAccountTransferBody body) {
        boolean isSuccess = accountService.transfer(body.getFrom(), body.getTo(), body.getSum());
        return isSuccess ? ResponseEntity.noContent().build() : ResponseEntity.badRequest().build();
    }
}
