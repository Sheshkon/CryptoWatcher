package com.example.cryptowatcher.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;

import lombok.Data;

import java.math.BigDecimal;

@Entity
@Data
public class UserCoin {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long cryptoId;
    private String symbol;
    private BigDecimal price = BigDecimal.ZERO;

    @ManyToOne()
    @JsonIgnoreProperties("userCoins")
    private User user;
}
