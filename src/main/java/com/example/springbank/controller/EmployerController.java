package com.example.springbank.controller;

import com.example.springbank.controller.dto.EmployerResponse;
import com.example.springbank.controller.dto.RequestEmployerCreateBody;
import com.example.springbank.service.EmployerService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/employers")
@AllArgsConstructor
public class EmployerController {
    private EmployerService employerService;

    @GetMapping
    public ResponseEntity<List<EmployerResponse>> getAllEmployers() {
        return ResponseEntity.ok(employerService.findAllEmployers());
    }

    @GetMapping("/{id}")
    public ResponseEntity<EmployerResponse> getEmployerById(@PathVariable Long id) {
        EmployerResponse employer = employerService.findEmployerById(id);
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
