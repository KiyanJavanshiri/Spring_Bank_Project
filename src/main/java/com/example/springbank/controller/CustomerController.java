package com.example.springbank.controller;

import com.example.springbank.controller.dto.RequestCustomerCreateAccount;
import com.example.springbank.controller.dto.RequestCustomerCreateBody;
import com.example.springbank.model.Account;
import com.example.springbank.model.Customer;
import com.example.springbank.service.AccountService;
import com.example.springbank.service.CustomerService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/customers")
public class CustomerController {
    private CustomerService customerService;
//    private AccountService accountService;

    public CustomerController(CustomerService customerService, AccountService accountService) {
        this.customerService = customerService;
//        this.accountService = accountService;
    }

    @GetMapping
    public ResponseEntity<List<Customer>> getAllCustomers() {
        return ResponseEntity.ok(customerService.getAllCustomers());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Customer> getCustomer(@PathVariable long id) {
        return ResponseEntity.ok(customerService.getCustomer(id));
    }

    @PostMapping
    public ResponseEntity<Customer> createCustomer(@Valid @RequestBody RequestCustomerCreateBody body) {
        Customer createdCustomer = customerService.createCustomer(body.getName(), body.getEmail(), body.getAge());
        return ResponseEntity.status(201).body(createdCustomer);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCustomer(@PathVariable long id) {
        customerService.deleteCustomer(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/account")
    public ResponseEntity<String> createAccount(@PathVariable long id, @RequestBody RequestCustomerCreateAccount body) {
        Customer customer = customerService.getCustomer(id);

        if(customer == null) {
            return ResponseEntity.badRequest().body("customer was not found");
        }

        customer.addAccount(new Account(body.getCurrency(), customer));
        customerService.saveCustomer(customer);

        return ResponseEntity.ok("Account created");
    }

    @DeleteMapping("/{id}/account/{accountId}")
    public ResponseEntity<String> deleteCustomerAccount(@PathVariable long id, @PathVariable long accountId) {
        Customer customer = customerService.getCustomer(id);
        if(customer == null) {
            return ResponseEntity.status(404).body("Customer with id " + id + " was not found");
        }

        Account account = customer.getAccounts()
                .stream()
                .filter(a -> a.getId().equals(accountId))
                .findFirst()
                .orElse(null);

        if(account == null) {
            return ResponseEntity.status(404).body("Account with id " + accountId + " was not found for customer with id " + id);
        }

        customer.removeAccount(account);
        customerService.saveCustomer(customer);

        return ResponseEntity.noContent().build();
    }
}
