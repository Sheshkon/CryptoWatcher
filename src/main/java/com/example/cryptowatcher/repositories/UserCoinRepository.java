package com.example.cryptowatcher.repositories;

import com.example.cryptowatcher.models.UserCoin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


import java.util.Optional;

@Repository
public interface UserCoinRepository extends JpaRepository<UserCoin, Long> {
    Optional<UserCoin> findByUserUsernameAndSymbol(String username, String symbol);
}
