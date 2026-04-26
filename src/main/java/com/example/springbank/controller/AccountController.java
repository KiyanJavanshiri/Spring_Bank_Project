package com.example.springbank.controller;

import com.example.springbank.annotation.LogController;
import com.example.springbank.controller.dto.AccountResponse;
import com.example.springbank.controller.dto.RequestAccountTransactionBody;
import com.example.springbank.controller.dto.RequestAccountTransferBody;
import com.example.springbank.model.Account;
import com.example.springbank.service.AccountService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@LogController
@RestController
@RequestMapping("/accounts")
@AllArgsConstructor
public class AccountController {
    private final AccountService accountService;

    @GetMapping
    public ResponseEntity<List<AccountResponse>> getAccounts() {
        return ResponseEntity.ok(accountService.getAllAccounts());
    }

    @PostMapping("/deposit")
    public ResponseEntity<AccountResponse> depositAccount(@Valid @RequestBody RequestAccountTransactionBody body) {
        AccountResponse account = accountService.deposit(body.getNumber(), body.getSum());

        if (account == null) {
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.ok(account);
    }

    @PostMapping("/withdrawal")
    public ResponseEntity<AccountResponse> withdrawalAccount(@Valid @RequestBody RequestAccountTransactionBody body) {
        AccountResponse account = accountService.withdrawal(body.getNumber(), body.getSum());

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
