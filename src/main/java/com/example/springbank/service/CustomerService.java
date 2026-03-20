package com.example.springbank.service;


import com.example.springbank.controller.dto.RequestCustomerCreateBody;
import com.example.springbank.model.Account;
import com.example.springbank.model.Currency;
import com.example.springbank.model.Customer;
import com.example.springbank.model.Employer;
import com.example.springbank.repository.CustomerRepository;
import com.example.springbank.repository.EmployerRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CustomerService {
    private CustomerRepository repository;
    private EmployerRepository employerRepository;

    public CustomerService(CustomerRepository repository, EmployerRepository employerRepository) {
        this.repository = repository;
        this.employerRepository = employerRepository;
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

    public void deleteCustomer(long id) {
        repository.deleteById(id);
    }

    public Account createAccount(Long customerId, Currency currency) {
        Customer customer = repository.findById(customerId)
                .orElseThrow(() -> new RuntimeException("Customer not found"));

        Account account = new Account(currency, customer);
        customer.addAccount(account);

        repository.save(customer);

        return account;
    }

    public boolean deleteAccount(Long customerId, Long accountId) {
        Customer customer = repository.findById(customerId)
                .orElseThrow(() -> new RuntimeException("Customer not found"));

        Optional<Account> account = customer.getAccounts()
                .stream()
                .filter(a -> a.getId().equals(accountId))
                .findFirst();

        if (account.isPresent()) {
            customer.removeAccount(account.get());
            repository.save(customer);
            return true;
        }

        return false;
    }

    public Customer updateCustomer(long id, RequestCustomerCreateBody body) {
        Customer customer = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Customer not found"));

        customer.setAge(body.getAge());
        customer.setName(body.getName());
        customer.setEmail(body.getEmail());

        repository.save(customer);

        return customer;
    }

    public void addEmployerToCustomer(Long customerId, Long employerId) {
        Customer customer = repository.findById(customerId)
                .orElseThrow(() -> new RuntimeException("Customer not found"));

        Employer employer = employerRepository.findById(employerId)
                .orElseThrow(() -> new RuntimeException("Employer not found"));

        customer.getEmployers().add(employer);
        employer.getCustomers().add(customer);

        repository.save(customer);
    }

    public void saveCustomer(Customer customer) {
        repository.save(customer);
    }
}
