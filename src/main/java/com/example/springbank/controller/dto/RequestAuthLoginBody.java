package com.example.springbank.controller.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RequestAuthLoginBody {
    @Email
    private String email;

    @Length(min = 6, max = 20)
    private String password;
}
