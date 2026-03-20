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
    private String name;
    private String address;

    public Employer() {}

    public Employer(String name, String address) {
        this.name = name;
        this.address = address;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
                ", name='" + name + '\'' +
                ", address='" + address + '\'' +
                '}';
    }
}
