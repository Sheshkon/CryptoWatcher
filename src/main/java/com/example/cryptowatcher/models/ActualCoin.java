package com.example.cryptowatcher.models;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;

@Entity
@Data
public class ActualCoin {
    @Id
    private Long id;
    @Column(unique = true)
    private String symbol;
    private BigDecimal price = BigDecimal.ZERO;
}
