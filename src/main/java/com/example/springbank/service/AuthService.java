package com.example.springbank.service;

import com.example.springbank.controller.dto.AuthResponse;
import com.example.springbank.controller.dto.RequestAuthLoginBody;
import com.example.springbank.controller.dto.RequestCustomerCreateBody;
import com.example.springbank.model.Customer;
import com.example.springbank.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final CustomerRepository customerRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public String register(RequestCustomerCreateBody body) {
        Customer customer = this.customerRepository.findByEmail(body.getEmail()).orElse(null);

        if(customer != null) {
            return "User is already exists";
        }

        String hashedPassword = passwordEncoder.encode(body.getPassword());
        Customer newCustomer = new Customer(body.getName(), body.getEmail(), hashedPassword, body.getPhoneNumber(), body.getAge());
        this.customerRepository.save(newCustomer);

        return "User registered successfully";
    }

    public AuthResponse login(RequestAuthLoginBody body) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(body.getEmail(), body.getPassword())
        );
        Customer customer = customerRepository.findByEmail(body.getEmail()).orElseThrow();
        String token = this.jwtService.generateToken(customer);
        return AuthResponse.builder().access_token(token).build();
    }
}
