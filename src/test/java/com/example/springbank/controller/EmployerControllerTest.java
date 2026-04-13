package com.example.springbank.controller;

import com.example.springbank.config.JwtAuthenticationFilter;
import com.example.springbank.controller.dto.EmployerResponse;
import com.example.springbank.controller.dto.RequestEmployerCreateBody;
import com.example.springbank.service.EmployerService;
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
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(EmployerController.class)
@AutoConfigureMockMvc(addFilters = false)
@DisplayName("EmployerController tests")
class EmployerControllerTest {
    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private EmployerService employerService;

    @MockitoBean
    private JwtAuthenticationFilter authenticationFilter;

    private EmployerResponse buildResponse(Long id, String name) {
        return EmployerResponse.builder()
                .id(id)
                .name(name)
                .build();
    }

    @Test
    @DisplayName("GET /employers - 200")
    void getAllEmployers_200() throws Exception {

        when(employerService.findAllEmployers())
                .thenReturn(List.of(
                        buildResponse(1L, "Google"),
                        buildResponse(2L, "Amazon")
                ));

        mockMvc.perform(get("/employers"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));
    }

    @Test
    @DisplayName("GET /employers/{id} - 00")
    void getEmployerById_200() throws Exception {

        when(employerService.findEmployerById(1L))
                .thenReturn(buildResponse(1L, "Google"));

        mockMvc.perform(get("/employers/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Google"));
    }

    @Test
    @DisplayName("GET /employers/{id} - 404")
    void getEmployerById_404() throws Exception {

        when(employerService.findEmployerById(anyLong()))
                .thenReturn(null);

        mockMvc.perform(get("/employers/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("POST /employers - 200")
    void createEmployer_200() throws Exception {

        RequestEmployerCreateBody req = new RequestEmployerCreateBody();
        req.setName("Google");

        doNothing().when(employerService).createEmployer(any());

        mockMvc.perform(post("/employers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isOk());
    }
}