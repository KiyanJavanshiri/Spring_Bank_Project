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

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @GetMapping
    public ResponseEntity<List<Customer>> getAllCustomers() {
        return ResponseEntity.ok(customerService.getAllCustomers());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Customer> getCustomer(@PathVariable long id) {
        Customer customer = customerService.getCustomer(id);
        return customer == null
                ? ResponseEntity.notFound().build()
                : ResponseEntity.ok(customer);
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
    public ResponseEntity<String> createCustomerAccount(@PathVariable long id,@Valid @RequestBody RequestCustomerCreateAccount body) {
        Account createdAccount = customerService.createAccount(id, body.getCurrency());

        return createdAccount == null ? ResponseEntity.notFound().build() : ResponseEntity.status(201).body("Account created");
    }

    @DeleteMapping("/{id}/account/{accountId}")
    public ResponseEntity<String> deleteCustomerAccount(@PathVariable long id, @PathVariable long accountId) {
        boolean isDeleted = customerService.deleteAccount(id, accountId);
        return !isDeleted ? ResponseEntity.badRequest().build() : ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateCustomer(@PathVariable long id,@Valid @RequestBody RequestCustomerCreateBody body) {
        Customer customer = customerService.getCustomer(id);

        if(customer == null) {
            return ResponseEntity.badRequest().build();
        }

        customer.setAge(body.getAge());
        customer.setName(body.getName());
        customer.setEmail(body.getEmail());

        customerService.saveCustomer(customer);

        return ResponseEntity.noContent().build();
    }
}
