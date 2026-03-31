package com.example.springbank.mapper;

import com.example.springbank.controller.dto.EmployerResponse;
import com.example.springbank.model.Employer;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface EmployerMapper {
    EmployerResponse toResponse(Employer employer);
}
