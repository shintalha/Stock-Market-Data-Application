package com.personal.stockmarketmanagement.model.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "market", indexes = {@Index(name = "code_index", columnList = "code", unique = true)})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Market {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "code")
    private String code;

    @Column(name = "symbol")
    private String symbol;

    @Column(name = "name")
    private String name;

    @Column(name = "country")
    private String country;

    @Column(name = "website")
    private String website;
}