package com.example.springbank.service;

import com.example.springbank.controller.dto.CustomerResponse;
import com.example.springbank.controller.dto.RequestCustomerCreateBody;
import com.example.springbank.mapper.CustomerMapper;
import com.example.springbank.model.Account;
import com.example.springbank.model.Currency;
import com.example.springbank.model.Customer;
import com.example.springbank.model.Employer;
import com.example.springbank.repository.CustomerRepository;
import com.example.springbank.repository.EmployerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("CustomerService tests")
class CustomerServiceTest {
    @Mock
    private CustomerRepository repository;

    @Mock
    private EmployerRepository employerRepository;

    @Mock
    private CustomerMapper customerMapper;

    @InjectMocks
    private CustomerService customerService;

    private Customer customer;
    private CustomerResponse response;

    @BeforeEach
    void setUp() {
        customer = new Customer("John", "john@mail.com", "pass", "1234567890", 25);
        customer.setId(1L);
        customer.setAccounts(new ArrayList<>());

        response = CustomerResponse.builder()
                .id(1L)
                .name("John")
                .email("john@mail.com")
                .build();
    }

    @Nested
    @DisplayName("getCustomer method")
    class GetCustomer {

        @Test
        @DisplayName("returns customer when found")
        void getCustomer_found() {
            when(repository.findById(1L)).thenReturn(Optional.of(customer));
            when(customerMapper.toResponse(customer)).thenReturn(response);

            CustomerResponse result = customerService.getCustomer(1L);

            assertThat(result.getId()).isEqualTo(1L);
            verify(repository).findById(1L);
        }

        @Test
        @DisplayName("returns null when not found")
        void getCustomer_notFound() {
            when(repository.findById(anyLong())).thenReturn(Optional.empty());

            CustomerResponse result = customerService.getCustomer(99L);

            assertThat(result).isNull();
        }
    }

    @Nested
    @DisplayName("getAllCustomers method")
    class GetAllCustomers {

        @Test
        @DisplayName("returns paged list")
        void getAllCustomers_success() {
            Customer c2 = new Customer("Bob", "bob@mail.com", "pass", "32123456778", 30);
            c2.setId(2L);

            Page<Customer> pageList = new PageImpl<>(List.of(customer, c2));

            when(repository.findAll(any(PageRequest.class)))
                    .thenReturn(pageList);

            when(customerMapper.toResponse(any()))
                    .thenAnswer(inv -> {
                        Customer c = inv.getArgument(0);
                        return CustomerResponse.builder()
                                .id(c.getId())
                                .name(c.getName())
                                .email(c.getEmail())
                                .build();
                    });

            List<CustomerResponse> result = customerService.getAllCustomers(0, 10);

            assertThat(result).hasSize(2);
            assertThat(result).extracting(CustomerResponse::getName)
                    .containsExactly("John", "Bob");
        }

        @Test
        @DisplayName("returns empty list when no customers")
        void getAllCustomers_empty() {
            Page<Customer> emptyPage = new PageImpl<>(List.of());

            when(repository.findAll(any(Pageable.class)))
                    .thenReturn(emptyPage);

            List<CustomerResponse> result = customerService.getAllCustomers(0, 10);

            assertThat(result).isEmpty();
        }
    }

    @Nested
    @DisplayName("createCustomer method")
    class CreateCustomer {

        @Test
        @DisplayName("creates and returns customer")
        void createCustomer_success() {
            when(repository.save(any(Customer.class))).thenReturn(customer);
            when(customerMapper.toResponse(any())).thenReturn(response);

            CustomerResponse result = customerService.createCustomer(
                    "John", "john@mail.com", 25, "pass", "1232345678"
            );

            assertThat(result.getName()).isEqualTo("John");
            verify(repository).save(any(Customer.class));
        }
    }

    @Nested
    @DisplayName("deleteCustomer method")
    class DeleteCustomer {

        @Test
        @DisplayName("deletes by id")
        void deleteCustomer_success() {
            customerService.deleteCustomer(1L);

            verify(repository).deleteById(1L);
        }
    }

    @Nested
    @DisplayName("createAccount method")
    class CreateAccount {

        @Test
        @DisplayName("creates account when customer exists")
        void createAccount_success() {
            when(repository.findById(1L)).thenReturn(Optional.of(customer));
            when(repository.save(any())).thenReturn(customer);

            Account account = customerService.createAccount(1L, Currency.USD);

            assertThat(account).isNotNull();
            verify(repository).save(customer);
        }

        @Test
        @DisplayName("throws when customer not found")
        void createAccount_notFound() {
            when(repository.findById(anyLong())).thenReturn(Optional.empty());

            assertThatThrownBy(() ->
                    customerService.createAccount(99L, Currency.USD)
            ).isInstanceOf(RuntimeException.class)
                    .hasMessageContaining("Customer not found");
        }
    }

    @Nested
    @DisplayName("deleteAccount method")
    class DeleteAccount {

        @Test
        @DisplayName("removes account when exists")
        void deleteAccount_success() {
            Account account = new Account(Currency.USD, customer);
            account.setId(10L);

            customer.getAccounts().add(account);

            when(repository.findById(1L)).thenReturn(Optional.of(customer));
            when(repository.save(any())).thenReturn(customer);

            boolean result = customerService.deleteAccount(1L, 10L);

            assertThat(result).isTrue();
            verify(repository).save(customer);
        }

        @Test
        @DisplayName("returns false when account not found")
        void deleteAccount_notFound() {
            when(repository.findById(1L)).thenReturn(Optional.of(customer));

            boolean result = customerService.deleteAccount(1L, 99L);

            assertThat(result).isFalse();
        }
    }

    @Nested
    @DisplayName("updateCustomer method")
    class UpdateCustomer {

        @Test
        @DisplayName("updates customer successfully")
        void updateCustomer_success() {
            RequestCustomerCreateBody body = new RequestCustomerCreateBody();
            body.setName("New Name");
            body.setEmail("new@mail.com");
            body.setAge(30);

            when(repository.findById(1L)).thenReturn(Optional.of(customer));
            when(repository.save(any())).thenReturn(customer);

            customerService.updateCustomer(1L, body);

            verify(repository).save(customer);
            assertThat(customer.getName()).isEqualTo("New Name");
        }

        @Test
        @DisplayName("throws when customer not found")
        void updateCustomer_notFound() {
            when(repository.findById(anyLong())).thenReturn(Optional.empty());

            assertThatThrownBy(() ->
                    customerService.updateCustomer(99L, new RequestCustomerCreateBody())
            ).isInstanceOf(RuntimeException.class);
        }
    }

    @Nested
    @DisplayName("addEmployerToCustomer method")
    class AddEmployerToCustomer {

        @Test
        @DisplayName("adds employer successfully")
        void addEmployer_success() {
            Employer employer = new Employer();
            employer.setId(2L);

            when(repository.findById(1L)).thenReturn(Optional.of(customer));
            when(employerRepository.findById(2L)).thenReturn(Optional.of(employer));

            customerService.addEmployerToCustomer(1L, 2L);

            verify(repository).save(customer);
            assertThat(customer.getEmployers()).contains(employer);
        }

        @Test
        @DisplayName("throws when employer not found")
        void addEmployer_notFound() {
            when(repository.findById(1L)).thenReturn(Optional.of(customer));
            when(employerRepository.findById(anyLong())).thenReturn(Optional.empty());

            assertThatThrownBy(() ->
                    customerService.addEmployerToCustomer(1L, 2L)
            ).isInstanceOf(RuntimeException.class)
                    .hasMessageContaining("Employer not found");
        }
    }

    @Test
    @DisplayName("saveCustomer method")
    void saveCustomer_success() {
        customerService.saveCustomer(customer);

        verify(repository).save(customer);
    }
}