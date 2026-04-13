package com.example.springbank.controller;
import com.example.springbank.config.JwtAuthenticationFilter;
import com.example.springbank.controller.dto.CustomerResponse;
import com.example.springbank.controller.dto.RequestCustomerCreateAccount;
import com.example.springbank.controller.dto.RequestCustomerCreateBody;
import com.example.springbank.model.Account;
import com.example.springbank.model.Currency;
import com.example.springbank.service.CustomerService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@WebMvcTest(CustomerController.class)
@AutoConfigureMockMvc(addFilters = false)
@DisplayName("CustomerController tests")
class CustomerControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private CustomerService customerService;

    @MockitoBean
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    private CustomerResponse buildResponse(Long id) {
        return CustomerResponse.builder()
                .id(id)
                .name("John")
                .email("john@example.com")
                .age(25)
                .phoneNumber("123456789")
                .build();
    }

    @Test
    @DisplayName("POST /customers - 201")
    void createCustomer_201() throws Exception {
        RequestCustomerCreateBody body = new RequestCustomerCreateBody();
        body.setName("John");
        body.setEmail("john@example.com");
        body.setAge(25);
        body.setPhoneNumber("12345678922");
        body.setPassword("12345");

        when(customerService.createCustomer(anyString(), anyString(), anyInt(), anyString(), anyString()))
                .thenReturn(buildResponse(1L));

        mockMvc.perform(post("/customers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(body)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    @DisplayName("POST /customers - 400")
    void createCustomer_400() throws Exception {
        RequestCustomerCreateBody body = new RequestCustomerCreateBody();
        body.setName("");

        mockMvc.perform(post("/customers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(body)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("GET /customers/{id} - 200")
    void getCustomer_200() throws Exception {
        when(customerService.getCustomer(1L)).thenReturn(buildResponse(1L));

        mockMvc.perform(get("/customers/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.email").value("john@example.com"));
    }

    @Test
    @DisplayName("GET /customers/{id} - 404")
    void getCustomer_404() throws Exception {
        when(customerService.getCustomer(anyLong())).thenReturn(null);

        mockMvc.perform(get("/customers/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("GET /customers - 200")
    void getAllCustomers_200() throws Exception {
        when(customerService.getAllCustomers(anyInt(), anyInt()))
                .thenReturn(List.of(buildResponse(1L), buildResponse(2L)));

        mockMvc.perform(get("/customers"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));
    }

    @Test
    @DisplayName("DELETE /customers/{id} - 204")
    void deleteCustomer_204() throws Exception {
        doNothing().when(customerService).deleteCustomer(1L);

        mockMvc.perform(delete("/customers/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("PUT /customers/{id} - 204")
    void updateCustomer_204() throws Exception {
        RequestCustomerCreateBody body = new RequestCustomerCreateBody();
        body.setName("Updated");
        body.setEmail("updated@example.com");
        body.setAge(25);
        body.setPhoneNumber("12345678922");
        body.setPassword("12345");

        doNothing().when(customerService).updateCustomer(eq(1L), any(RequestCustomerCreateBody.class));

        mockMvc.perform(put("/customers/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(body)))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("POST /customers/{id}/account - 201")
    void createAccount_201() throws Exception {
        RequestCustomerCreateAccount body = new RequestCustomerCreateAccount();
        body.setCurrency(Currency.USD);

        when(customerService.createAccount(eq(1L), any()))
                .thenReturn(new Account());

        mockMvc.perform(post("/customers/1/account")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(body)))
                .andExpect(status().isCreated());
    }

    @Test
    @DisplayName("DELETE account - 204")
    void deleteAccount_204() throws Exception {
        when(customerService.deleteAccount(1L, 1L)).thenReturn(true);

        mockMvc.perform(delete("/customers/1/account/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("POST add employer - 200")
    void addEmployer_200() throws Exception {
        doNothing().when(customerService).addEmployerToCustomer(1L, 2L);

        mockMvc.perform(post("/customers/1/employers/2"))
                .andExpect(status().isOk());
    }
}
