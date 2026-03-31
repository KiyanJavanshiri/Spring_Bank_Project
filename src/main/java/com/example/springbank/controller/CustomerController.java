package com.example.springbank.controller;

import com.example.springbank.controller.dto.CustomerResponse;
import com.example.springbank.controller.dto.RequestCustomerCreateAccount;
import com.example.springbank.controller.dto.RequestCustomerCreateBody;
import com.example.springbank.model.Account;
import com.example.springbank.model.Customer;
import com.example.springbank.service.AccountService;
import com.example.springbank.service.CustomerService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/customers")
@AllArgsConstructor
public class CustomerController {
    private CustomerService customerService;

    @GetMapping
    public ResponseEntity<List<CustomerResponse>> getAllCustomers() {
        return ResponseEntity.ok(customerService.getAllCustomers());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CustomerResponse> getCustomer(@PathVariable long id) {
        CustomerResponse customer = customerService.getCustomer(id);
        return customer == null
                ? ResponseEntity.notFound().build()
                : ResponseEntity.ok(customer);
    }

    @PostMapping
    public ResponseEntity<CustomerResponse> createCustomer(@Valid @RequestBody RequestCustomerCreateBody body) {
        CustomerResponse createdCustomer = customerService.createCustomer(body.getName(), body.getEmail(),body.getAge(), body.getPhoneNumber(), body.getPassword());
        return ResponseEntity.status(201).body(createdCustomer);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCustomer(@PathVariable long id) {
        customerService.deleteCustomer(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/account")
    public ResponseEntity<String> createCustomerAccount(@PathVariable long id, @Valid @RequestBody RequestCustomerCreateAccount body) {
        Account createdAccount = customerService.createAccount(id, body.getCurrency());

        return createdAccount == null ? ResponseEntity.notFound().build() : ResponseEntity.status(201).body("Account created");
    }

    @DeleteMapping("/{id}/account/{accountId}")
    public ResponseEntity<Void> deleteCustomerAccount(@PathVariable long id, @PathVariable long accountId) {
        boolean isDeleted = customerService.deleteAccount(id, accountId);
        return !isDeleted ? ResponseEntity.badRequest().build() : ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateCustomer(@PathVariable long id, @Valid @RequestBody RequestCustomerCreateBody body) {
        customerService.updateCustomer(id, body);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{customerId}/employers/{employerId}")
    public ResponseEntity<Void> addEmployer(
            @PathVariable Long customerId,
            @PathVariable Long employerId) {

        customerService.addEmployerToCustomer(customerId, employerId);
        return ResponseEntity.ok().build();
    }
}
