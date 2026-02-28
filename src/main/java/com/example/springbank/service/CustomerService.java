package com.example.springbank.service;


import com.example.springbank.model.Customer;
import com.example.springbank.repository.CustomerRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CustomerService {
    private CustomerRepository repository;

    public CustomerService(CustomerRepository repository) {
        this.repository = repository;
    }

    public Customer getCustomer(long id) {
        Optional<Customer> customer = repository.findById(id);
        return customer.orElse(null);
    }

    public List<Customer> getAllCustomers() {
        return repository.findAll();
    }

    public Customer createCustomer(String name, String email, int age) {
        Customer customer = new Customer(name, email, age);
        repository.save(customer);
        return customer;
    }

//    public void changeCustomerData(long id) {
//
//    }

//    public void createAccount(long id) {
//
//    }
}
