package com.example.springbank.service;

import com.example.springbank.controller.dto.EmployerResponse;
import com.example.springbank.mapper.EmployerMapper;
import com.example.springbank.model.Employer;
import com.example.springbank.repository.EmployerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import com.example.springbank.controller.dto.RequestEmployerCreateBody;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("EmployerService tests")
class EmployerServiceTest {
    @Mock
    private EmployerRepository repository;

    @Mock
    private EmployerMapper employerMapper;

    @InjectMocks
    private EmployerService employerService;

    private Employer employer;
    private EmployerResponse response;

    @BeforeEach
    void setUp() {
        employer = new Employer("Google", "USA");
        employer.setId(1L);

        response = EmployerResponse.builder()
                .id(1L)
                .name("Google")
                .address("USA")
                .build();
    }

    @Nested
    @DisplayName("findAllEmployers method")
    class FindAllEmployers {

        @Test
        @DisplayName("returns list of employers")
        void findAll_success() {
            Employer e2 = new Employer("Amazon", "USA");
            e2.setId(2L);

            when(repository.findAll()).thenReturn(List.of(employer, e2));

            when(employerMapper.toResponse(any()))
                    .thenAnswer(inv -> {
                        Employer e = inv.getArgument(0);
                        return EmployerResponse.builder()
                                .id(e.getId())
                                .name(e.getName())
                                .address(e.getAddress())
                                .build();
                    });

            List<EmployerResponse> result = employerService.findAllEmployers();

            assertThat(result).hasSize(2);
            assertThat(result).extracting(EmployerResponse::getName)
                    .containsExactly("Google", "Amazon");
        }

        @Test
        @DisplayName("returns empty list when no employers")
        void findAll_empty() {
            when(repository.findAll()).thenReturn(List.of());

            assertThat(employerService.findAllEmployers()).isEmpty();
        }
    }

    @Nested
    @DisplayName("findEmployerById method")
    class FindById {

        @Test
        @DisplayName("returns employer when found")
        void findById_found() {
            when(repository.findById(1L)).thenReturn(Optional.of(employer));
            when(employerMapper.toResponse(employer)).thenReturn(response);

            EmployerResponse result = employerService.findEmployerById(1L);

            assertThat(result.getId()).isEqualTo(1L);
            verify(repository).findById(1L);
        }

        @Test
        @DisplayName("returns null when not found")
        void findById_notFound() {
            when(repository.findById(anyLong())).thenReturn(Optional.empty());

            EmployerResponse result = employerService.findEmployerById(99L);

            assertThat(result).isNull();
        }
    }

    @Nested
    @DisplayName("createEmployer method")
    class CreateEmployer {

        @Test
        @DisplayName("creates and saves employer")
        void create_success() {
            RequestEmployerCreateBody body = new RequestEmployerCreateBody();
            body.setName("Google");
            body.setAddress("USA");

            when(repository.save(any())).thenReturn(employer);

            employerService.createEmployer(body);

            verify(repository).save(any(Employer.class));
        }
    }
}