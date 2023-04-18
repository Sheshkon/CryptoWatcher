package com.example.cryptowatcher.services;

import com.example.cryptowatcher.models.User;
import com.example.cryptowatcher.models.UserCoin;

import com.example.cryptowatcher.repositories.UserCoinRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class UserCoinService {

    private final UserCoinRepository userCoinRepository;

    public List<UserCoin> getCoins() {
        return userCoinRepository.findAll();
    }

    public Optional<UserCoin> getByUserUsernameAndSymbol(String username, String symbol){
        return userCoinRepository.findByUserUsernameAndSymbol(username, symbol);
    }

    public UserCoin createUserCoin(User user, Long cryptoId, String symbol, BigDecimal price) {
        UserCoin userCoin = new UserCoin();
        userCoin.setCryptoId(cryptoId);
        userCoin.setSymbol(symbol);
        userCoin.setPrice(price);
        userCoin.setUser(user);
        return userCoinRepository.save(userCoin);
    }

    public UserCoin updateUserCoin(UserCoin userCoin) {
        return userCoinRepository.save(userCoin);
    }
}
