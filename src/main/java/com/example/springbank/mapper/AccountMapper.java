package com.example.springbank.mapper;

import com.example.springbank.controller.dto.AccountResponse;
import com.example.springbank.model.Account;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AccountMapper {
    AccountResponse toResponse(Account account);
}
