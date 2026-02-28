package com.example.springbank;

import com.example.springbank.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SpringBankApplication   {
	public static void main(String[] args) {
		SpringApplication.run(SpringBankApplication.class, args);
	}
}
