package com.example.cryptowatcher.repositories;

import com.example.cryptowatcher.models.ActualCoin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ActualCoinRepository extends JpaRepository<ActualCoin, Long> {
    List<ActualCoin> findAll();
    Optional<ActualCoin> findById(Long Id);
    Optional<ActualCoin> findBySymbol(String symbol);
}
