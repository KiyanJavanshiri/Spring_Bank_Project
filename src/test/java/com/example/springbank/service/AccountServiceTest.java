package com.example.springbank.service;

import com.example.springbank.controller.dto.AccountResponse;
import com.example.springbank.mapper.AccountMapper;
import com.example.springbank.model.Account;
import com.example.springbank.repository.AccountRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("AccountService tests")
class AccountServiceTest {
    @Mock
    private AccountRepository accountRepository;

    @Mock
    private AccountMapper accountMapper;

    @InjectMocks
    private AccountService accountService;

    private Account account;
    private AccountResponse response;

    @BeforeEach
    void setUp() {
        account = new Account();
        account.setId(1L);
        account.setNumber("111");
        account.setBalance(100.0);

        response = AccountResponse.builder()
                .id(1L)
                .number("111")
                .balance(100.0)
                .build();
    }

    @Nested
    @DisplayName("getAccount method")
    class GetAccount {

        @Test
        @DisplayName("returns account when found")
        void getAccount_found() {
            when(accountRepository.findById(1L)).thenReturn(Optional.of(account));
            when(accountMapper.toResponse(account)).thenReturn(response);

            AccountResponse result = accountService.getAccount(1L);

            assertThat(result.getId()).isEqualTo(1L);
            verify(accountRepository).findById(1L);
        }

        @Test
        @DisplayName("returns null when not found")
        void getAccount_notFound() {
            when(accountRepository.findById(anyLong())).thenReturn(Optional.empty());

            AccountResponse result = accountService.getAccount(99L);

            assertThat(result).isNull();
        }
    }

    // ------------------------------------------------------------------ getAllAccounts
    @Nested
    @DisplayName("getAllAccounts method")
    class GetAllAccounts {

        @Test
        @DisplayName("returns all accounts")
        void getAllAccounts_success() {
            Account acc2 = new Account();
            acc2.setId(2L);
            acc2.setNumber("222");
            acc2.setBalance(200.0);

            when(accountRepository.findAll()).thenReturn(List.of(account, acc2));

            when(accountMapper.toResponse(any()))
                    .thenAnswer(inv -> {
                        Account a = inv.getArgument(0);
                        return AccountResponse.builder()
                                .id(a.getId())
                                .number(a.getNumber())
                                .balance(a.getBalance())
                                .build();
                    });

            List<AccountResponse> result = accountService.getAllAccounts();

            assertThat(result).hasSize(2);
            assertThat(result).extracting(AccountResponse::getNumber)
                    .containsExactly("111", "222");
        }

        @Test
        @DisplayName("returns empty list")
        void getAllAccounts_empty() {
            when(accountRepository.findAll()).thenReturn(List.of());

            assertThat(accountService.getAllAccounts()).isEmpty();
        }
    }

    @Nested
    @DisplayName("deposit method")
    class Deposit {

        @Test
        @DisplayName("adds money successfully")
        void deposit_success() {
            when(accountRepository.findByNumber("111")).thenReturn(Optional.of(account));
            when(accountMapper.toResponse(any())).thenReturn(response);

            AccountResponse result = accountService.deposit("111", 50);

            assertThat(result).isNotNull();
            assertThat(account.getBalance()).isEqualTo(150.0);

            verify(accountRepository).save(account);
        }

        @Test
        @DisplayName("returns null when account not found")
        void deposit_notFound() {
            when(accountRepository.findByNumber(anyString())).thenReturn(Optional.empty());

            AccountResponse result = accountService.deposit("999", 50);

            assertThat(result).isNull();
        }
    }

    @Nested
    @DisplayName("withdrawal method")
    class Withdrawal {

        @Test
        @DisplayName("withdraws successfully")
        void withdrawal_success() {
            when(accountRepository.findByNumber("111")).thenReturn(Optional.of(account));
            when(accountMapper.toResponse(any())).thenReturn(response);

            AccountResponse result = accountService.withdrawal("111", 40);

            assertThat(result).isNotNull();
            assertThat(account.getBalance()).isEqualTo(60.0);

            verify(accountRepository).save(account);
        }

        @Test
        @DisplayName("returns null when insufficient balance")
        void withdrawal_insufficient() {
            when(accountRepository.findByNumber("111")).thenReturn(Optional.of(account));

            AccountResponse result = accountService.withdrawal("111", 999);

            assertThat(result).isNull();
        }

        @Test
        @DisplayName("returns null when account not found")
        void withdrawal_notFound() {
            when(accountRepository.findByNumber(anyString())).thenReturn(Optional.empty());

            assertThat(accountService.withdrawal("999", 10)).isNull();
        }
    }

    @Nested
    @DisplayName("transfer method")
    class Transfer {

        @Test
        @DisplayName("transfers money successfully")
        void transfer_success() {
            Account from = new Account();
            from.setNumber("111");
            from.setBalance(100.00);

            Account to = new Account();
            to.setNumber("222");
            to.setBalance(50.00);

            when(accountRepository.findByNumber("111")).thenReturn(Optional.of(from));
            when(accountRepository.findByNumber("222")).thenReturn(Optional.of(to));

            boolean result = accountService.transfer("111", "222", 30);

            assertThat(result).isTrue();
            assertThat(from.getBalance()).isEqualTo(70);
            assertThat(to.getBalance()).isEqualTo(80);

            verify(accountRepository).save(from);
            verify(accountRepository).save(to);
        }

        @Test
        @DisplayName("fails when from account not found")
        void transfer_fromNotFound() {
            when(accountRepository.findByNumber("111")).thenReturn(Optional.empty());

            boolean result = accountService.transfer("111", "222", 30);

            assertThat(result).isFalse();
        }

        @Test
        @DisplayName("fails when insufficient balance")
        void transfer_insufficient() {
            Account from = new Account();
            from.setNumber("111");
            from.setBalance(10.00);

            Account to = new Account();
            to.setNumber("222");
            to.setBalance(50.00);

            when(accountRepository.findByNumber("111")).thenReturn(Optional.of(from));
            when(accountRepository.findByNumber("222")).thenReturn(Optional.of(to));

            boolean result = accountService.transfer("111", "222", 999);

            assertThat(result).isFalse();
        }
    }

    @Test
    @DisplayName("saveAccount method")
    void saveAccount_success() {
        accountService.saveAccount(account);

        verify(accountRepository).save(account);
    }
}