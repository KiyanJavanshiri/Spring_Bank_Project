package com.example.springbank.mapper;

import com.example.springbank.controller.dto.CustomerResponse;
import com.example.springbank.controller.dto.RequestCustomerCreateAccount;
import com.example.springbank.controller.dto.RequestCustomerCreateBody;
import com.example.springbank.model.Customer;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {AccountMapper.class, EmployerMapper.class})
public interface CustomerMapper {
    CustomerResponse toResponse(Customer customer);
}
