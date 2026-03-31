package com.example.springbank.service;

import com.example.springbank.controller.dto.EmployerResponse;
import com.example.springbank.controller.dto.RequestEmployerCreateBody;
import com.example.springbank.mapper.EmployerMapper;
import com.example.springbank.model.Employer;
import com.example.springbank.repository.EmployerRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class EmployerService {
    private EmployerRepository repository;
    private EmployerMapper employerMapper;

    public List<EmployerResponse> findAllEmployers() {
        return repository.findAll().stream().map(employerMapper::toResponse).collect(Collectors.toList());
    }

    public EmployerResponse findEmployerById(Long id) {
        Employer employer = repository.findById(id).orElse(null);
        return employer == null ? null : employerMapper.toResponse(employer);
    }

    public EmployerResponse createEmployer(RequestEmployerCreateBody body) {
        Employer employer = new Employer(body.getName(), body.getAddress());
        repository.save(employer);
        return employerMapper.toResponse(employer);
    }
}
