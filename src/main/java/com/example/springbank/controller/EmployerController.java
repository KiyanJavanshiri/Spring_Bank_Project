package com.example.springbank.controller;

import com.example.springbank.controller.dto.RequestEmployerCreateBody;
import com.example.springbank.model.Employer;
import com.example.springbank.service.EmployerService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/employers")
public class EmployerController {
    private EmployerService employerService;

    public EmployerController(EmployerService employerService) {
        this.employerService = employerService;
    }

    @GetMapping
    public ResponseEntity<List<Employer>> getAllEmployers() {
        return ResponseEntity.ok(employerService.findAllEmployers());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Employer> getEmployerById(@PathVariable Long id) {
        Employer employer = employerService.findEmployerById(id);
        return employer == null
                ? ResponseEntity.notFound().build()
                : ResponseEntity.ok(employer);
    }

    @PostMapping
    public ResponseEntity<Void> createEmployee(@RequestBody RequestEmployerCreateBody body) {
        employerService.createEmployer(body);
        return ResponseEntity.ok().build();
    }
}
