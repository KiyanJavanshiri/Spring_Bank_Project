package com.example.springbank.controller;

import com.example.springbank.controller.dto.AuthResponse;
import com.example.springbank.controller.dto.RequestAuthLoginBody;
import com.example.springbank.controller.dto.RequestCustomerCreateBody;
import com.example.springbank.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController()
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody RequestCustomerCreateBody body) {
        return ResponseEntity.ok(this.authService.register(body));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody RequestAuthLoginBody body) {
        return ResponseEntity.ok(this.authService.login(body));
    }
}
