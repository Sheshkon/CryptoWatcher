package com.example.cryptowatcher.services;

import com.example.cryptowatcher.models.User;
import com.example.cryptowatcher.models.UserCoin;
import com.example.cryptowatcher.repositories.UserCoinRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@SpringBootTest
@ActiveProfiles("test")
public class UserCoinServiceTest {

    private UserCoinRepository userCoinRepository;
    private UserCoinService userCoinService;
    private User user;
    private Long cryptoId;
    private String symbol;
    private BigDecimal price;

    @BeforeEach
    void setUp() {
        userCoinRepository = Mockito.mock(UserCoinRepository.class);
        userCoinService = new UserCoinService(userCoinRepository);
        user = new User();
        cryptoId = 1L;
        symbol = "BTC";
        price = new BigDecimal("50000.00");
    }

    @Test
    void testGetCoins() {
        List<UserCoin> coins = List.of(new UserCoin(), new UserCoin());
        Mockito.when(userCoinRepository.findAll()).thenReturn(coins);

        Assertions.assertEquals(coins, userCoinService.getCoins());
        Mockito.verify(userCoinRepository).findAll();
    }

    @Test
    void testGetByUserUsernameAndSymbol() {
        UserCoin userCoin = new UserCoin();
        Mockito.when(userCoinRepository.findByUserUsernameAndSymbol(Mockito.anyString(), Mockito.anyString()))
                .thenReturn(Optional.of(userCoin));

        Optional<UserCoin> result = userCoinService.getByUserUsernameAndSymbol("john_doe", "BTC");
        Assertions.assertEquals(Optional.of(userCoin), result);
        Mockito.verify(userCoinRepository).findByUserUsernameAndSymbol("john_doe", "BTC");
    }

    @Test
    void testCreateUserCoin() {
        UserCoin userCoin = new UserCoin();
        Mockito.when(userCoinRepository.save(Mockito.any(UserCoin.class))).thenReturn(userCoin);

        UserCoin result = userCoinService.createUserCoin(user, cryptoId, symbol, price);
        Assertions.assertEquals(userCoin, result);
        Mockito.verify(userCoinRepository).save(Mockito.any(UserCoin.class));
    }

    @Test
    void testUpdateUserCoin() {
        UserCoin userCoin = new UserCoin();
        Mockito.when(userCoinRepository.save(Mockito.any(UserCoin.class))).thenReturn(userCoin);

        UserCoin result = userCoinService.updateUserCoin(userCoin);
        Assertions.assertEquals(userCoin, result);
        Mockito.verify(userCoinRepository).save(Mockito.any(UserCoin.class));
    }
}