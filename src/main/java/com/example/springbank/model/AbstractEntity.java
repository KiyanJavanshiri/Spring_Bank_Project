package com.example.springbank.model;

import jakarta.persistence.*;

@Entity
@Table(name = "abstract_entity")
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class AbstractEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    protected AbstractEntity(){}

    protected AbstractEntity(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "id=" + id + ",";
    }
}
