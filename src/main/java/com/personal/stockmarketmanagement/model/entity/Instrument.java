package com.personal.stockmarketmanagement.model.entity;

import jakarta.annotation.Nonnull;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "instrument")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Instrument {
    @Id
    @Column(name = "id")
    private long id;

    @Column(name = "symbol")
    @Nonnull
    private String symbol;

    @Column(name = "full_name")
    private String name;

    @Column(name = "simple_name")
    private String customName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "market_id")
    private Market market;
}