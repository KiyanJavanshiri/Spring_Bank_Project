package com.example.springbank.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "employers")
@PrimaryKeyJoinColumn(name = "employer_id")
public class Employer extends AbstractEntity {
    @JsonIgnore
    @ManyToMany(mappedBy = "employers")
    private Set<Customer> customers = new HashSet<>();
    private String company;
    private String address;

    public Employer() {}

    public Employer(String company, String address) {
        this.company = company;
        this.address = address;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Set<Customer> getCustomers() {
        return customers;
    }

    public void setCustomers(Set<Customer> customers) {
        this.customers = customers;
    }

    @Override
    public String toString() {
        return "Employer{" +
                super.toString() +
                ", company='" + company + '\'' +
                ", address='" + address + '\'' +
                '}';
    }
}
