package com.example.springbank.controller;

import com.example.springbank.config.JwtAuthenticationFilter;
import com.example.springbank.controller.dto.AccountResponse;
import com.example.springbank.controller.dto.RequestAccountTransactionBody;
import com.example.springbank.controller.dto.RequestAccountTransferBody;
import com.example.springbank.service.AccountService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

import static org.mockito.ArgumentMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.List;

import static org.mockito.Mockito.when;

@WebMvcTest(AccountController.class)
@AutoConfigureMockMvc(addFilters = false)
@DisplayName("AccountController tests")
class AccountControllerTest {
    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private AccountService accountService;

    @MockitoBean
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    private AccountResponse buildResponse(String number, Double balance) {
        return AccountResponse.builder()
                .number(number)
                .balance(balance)
                .build();
    }

    @Test
    @DisplayName("GET /accounts - 200")
    void getAccounts_200() throws Exception {
        when(accountService.getAllAccounts())
                .thenReturn(List.of(
                        buildResponse("111", 100.00),
                        buildResponse("222", 200.00)
                ));

        mockMvc.perform(get("/accounts"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));
    }

    @Test
    @DisplayName("POST /accounts/deposit - 200 ")
    void deposit_200() throws Exception {
        RequestAccountTransactionBody req = new RequestAccountTransactionBody();
        req.setNumber("111");
        req.setSum(50.00);

        when(accountService.deposit("111", 50.00))
                .thenReturn(buildResponse("111", 150.00));

        mockMvc.perform(post("/accounts/deposit")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.number").value("111"));
    }

    @Test
    @DisplayName("POST /accounts/deposit - 400")
    void deposit_400() throws Exception {
        RequestAccountTransactionBody req = new RequestAccountTransactionBody();
        req.setNumber("111");
        req.setSum(50.00);

        when(accountService.deposit(anyString(), any(Double.class)))
                .thenReturn(null);

        mockMvc.perform(post("/accounts/deposit")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("POST /accounts/withdrawal - 200")
    void withdrawal_200() throws Exception {
        RequestAccountTransactionBody req = new RequestAccountTransactionBody();
        req.setNumber("111");
        req.setSum(30.00);

        when(accountService.withdrawal("111",30.00))
                .thenReturn(buildResponse("111", 70.00));

        mockMvc.perform(post("/accounts/withdrawal")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.balance").value(70));
    }

    @Test
    @DisplayName("POST /accounts/withdrawal - 400")
    void withdrawal_400() throws Exception {
        RequestAccountTransactionBody req = new RequestAccountTransactionBody();
        req.setNumber("111");
        req.setSum(999.00);

        when(accountService.withdrawal(anyString(), any(Double.class)))
                .thenReturn(null);

        mockMvc.perform(post("/accounts/withdrawal")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("POST /accounts/transfer - 204")
    void transfer_204() throws Exception {
        RequestAccountTransferBody req = new RequestAccountTransferBody();
        req.setFrom("111");
        req.setTo("222");
        req.setSum(50.00);

        when(accountService.transfer("111", "222", 50.00))
                .thenReturn(true);

        mockMvc.perform(post("/accounts/transfer")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("POST /accounts/transfer - 400")
    void transfer_400() throws Exception {
        RequestAccountTransferBody req = new RequestAccountTransferBody();
        req.setFrom("111");
        req.setTo("222");
        req.setSum(50.00);

        when(accountService.transfer(anyString(), anyString(), any(Double.class)))
                .thenReturn(false);

        mockMvc.perform(post("/accounts/transfer")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isBadRequest());
    }
}