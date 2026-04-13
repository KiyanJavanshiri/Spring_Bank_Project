package com.example.springbank.controller;

import com.example.springbank.config.JwtAuthenticationFilter;
import com.example.springbank.controller.dto.RequestCustomerCreateBody;
import com.example.springbank.service.AuthService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;
import com.example.springbank.controller.dto.RequestAuthLoginBody;
import com.example.springbank.controller.dto.AuthResponse;

import static org.mockito.ArgumentMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.mockito.Mockito.*;

@WebMvcTest(AuthController.class)
@AutoConfigureMockMvc(addFilters = false)
@DisplayName("AuthController tests")
class AuthControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private AuthService authService;

    @MockitoBean
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Test
    @DisplayName("POST /auth/register - 200")
    void register_200() throws Exception {

        RequestCustomerCreateBody body = new RequestCustomerCreateBody();
        body.setName("John");
        body.setEmail("john@mail.com");
        body.setPassword("123");
        body.setPhoneNumber("11123456789");
        body.setAge(25);

        when(authService.register(any()))
                .thenReturn("User registered successfully");

        mockMvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(body)))
                .andExpect(status().isOk())
                .andExpect(content().string("User registered successfully"));
    }

    @Test
    @DisplayName("POST /auth/login - 200")
    void login_200() throws Exception {

        RequestAuthLoginBody body = new RequestAuthLoginBody();
        body.setEmail("john@mail.com");
        body.setPassword("123");

        AuthResponse response = AuthResponse.builder()
                .access_token("jwt-token")
                .build();

        when(authService.login(any()))
                .thenReturn(response);

        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(body)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.access_token").value("jwt-token"));
    }
}