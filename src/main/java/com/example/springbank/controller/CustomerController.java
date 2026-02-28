package com.example.springbank.controller;

import com.example.springbank.controller.dto.RequestCustomerCreateBody;
import com.example.springbank.model.Customer;
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
        return ResponseEntity.ok(customerService.getCustomer(id));
    }

    @PostMapping
    public ResponseEntity<Customer> createCustomer(@Valid @RequestBody RequestCustomerCreateBody body) {
        Customer createdCustomer = customerService.createCustomer(body.getName(), body.getEmail(), body.getAge());
        return ResponseEntity.status(201).body(createdCustomer);
    }
}
