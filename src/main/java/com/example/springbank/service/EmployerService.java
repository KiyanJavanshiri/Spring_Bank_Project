package com.example.springbank.service;

import com.example.springbank.controller.dto.RequestEmployerCreateBody;
import com.example.springbank.model.Employer;
import com.example.springbank.repository.EmployerRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EmployerService {
    private EmployerRepository repository;

    public EmployerService(EmployerRepository repository) {
        this.repository = repository;
    }

    public List<Employer> findAllEmployers() {
        return repository.findAll();
    }

    public Employer findEmployerById(Long id) {
        Optional<Employer> employer = repository.findById(id);
        return employer.orElse(null);
    }

    public Employer createEmployer(RequestEmployerCreateBody body) {
        Employer employer = new Employer(body.getName(), body.getAddress());
        repository.save(employer);
        return employer;
    }
}
