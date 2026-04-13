package com.example.springbank.service;

import com.example.springbank.controller.dto.AuthResponse;
import com.example.springbank.controller.dto.RequestCustomerCreateBody;
import com.example.springbank.model.Customer;
import com.example.springbank.repository.CustomerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import com.example.springbank.controller.dto.RequestAuthLoginBody;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("AuthService tests")
class AuthServiceTest {
    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtService jwtService;

    @Mock
    private AuthenticationManager authenticationManager;

    @InjectMocks
    private AuthService authService;

    private Customer customer;

    @BeforeEach
    void setUp() {
        customer = new Customer(
                "John",
                "john@mail.com",
                "hashed",
                "1234567890",
                25
        );
        customer.setId(1L);
    }

    @Nested
    @DisplayName("register method")
    class Register {

        @Test
        @DisplayName("returns message when user already exists")
        void register_alreadyExists() {
            RequestCustomerCreateBody body = new RequestCustomerCreateBody();
            body.setName("John");
            body.setEmail("john@example.com");
            body.setAge(25);
            body.setPhoneNumber("12345678922");
            body.setPassword("12345");

            when(customerRepository.findByEmail(body.getEmail()))
                    .thenReturn(Optional.of(customer));

            String result = authService.register(body);

            assertThat(result).isEqualTo("User is already exists");
            verify(customerRepository, never()).save(any());
        }

        @Test
        @DisplayName("registers new user successfully")
        void register_success() {
            RequestCustomerCreateBody body = new RequestCustomerCreateBody();
            body.setName("John");
            body.setEmail("john@mail.com");
            body.setPassword("123");
            body.setPhoneNumber("111");
            body.setAge(25);

            when(customerRepository.findByEmail("john@mail.com"))
                    .thenReturn(Optional.empty());

            when(passwordEncoder.encode("123")).thenReturn("hashed");

            when(customerRepository.save(any(Customer.class)))
                    .thenAnswer(inv -> inv.getArgument(0));

            String result = authService.register(body);

            assertThat(result).isEqualTo("User registered successfully");

            verify(passwordEncoder).encode("123");
            verify(customerRepository).save(any(Customer.class));
        }
    }

    @Nested
    @DisplayName("login method")
    class Login {

        @Test
        @DisplayName("returns JWT token on success")
        void login_success() {
            RequestAuthLoginBody body = new RequestAuthLoginBody();
            body.setEmail("john@mail.com");
            body.setPassword("1234567");

            when(customerRepository.findByEmail("john@mail.com"))
                    .thenReturn(Optional.of(customer));

            when(jwtService.generateToken(customer))
                    .thenReturn("jwt-token");

            AuthResponse response = authService.login(body);

            assertThat(response.getAccess_token()).isEqualTo("jwt-token");

            verify(authenticationManager).authenticate(any());
            verify(jwtService).generateToken(customer);
        }
    }
}