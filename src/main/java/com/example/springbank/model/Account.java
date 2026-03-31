package com.example.springbank.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "accounts")
@PrimaryKeyJoinColumn(name = "account_id")
@Getter
@Setter
@NoArgsConstructor
@RequiredArgsConstructor
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class Account extends AbstractEntity {
    private String number = UUID.randomUUID().toString();

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @NonNull
    private Currency currency;

    private Double balance = (double) 0;

    @ManyToOne
    @JoinColumn(name = "customer_id")
    @JsonBackReference
    @NonNull
    private Customer customer;
}
