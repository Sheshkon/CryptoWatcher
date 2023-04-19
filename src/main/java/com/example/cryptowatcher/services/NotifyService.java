package com.example.cryptowatcher.services;

import com.example.cryptowatcher.exceptions.CoinNotAvailableException;
import com.example.cryptowatcher.models.ActualCoin;
import com.example.cryptowatcher.models.User;
import com.example.cryptowatcher.models.UserCoin;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Optional;

@Log4j2
@Service
@AllArgsConstructor
public class NotifyService {

    private final UserCoinService userCoinService;
    private final ActualCoinService actualCoinService;
    private final UserService userService;


    public String register(String username, String symbol) throws CoinNotAvailableException {
        Optional<User> userOptional = userService.getUserByUsername(username);
        User user = userOptional.orElseGet(() -> userService.createUser(username));

        Optional<ActualCoin> actualCoinOptional = actualCoinService.getBySymbol(symbol);

        ActualCoin actualCoin = actualCoinOptional.orElseThrow(() -> new CoinNotAvailableException(symbol));

        Optional<UserCoin> userCoinOptional = userCoinService.getByUserUsernameAndSymbol(username, symbol);

        if (userCoinOptional.isEmpty()) {
            userCoinService.createUserCoin(user, actualCoin.getId(), symbol, actualCoin.getPrice());
            return "User coin created";
        }

        UserCoin userCoin = userCoinOptional.get();
        userCoin.setPrice(actualCoin.getPrice());
        userCoinService.updateUserCoin(userCoin);

        return "User coin updated";
    }


    public void notifyUser() {
        List<UserCoin> userCoins = userCoinService.getCoins();
        for (UserCoin userCoin : userCoins) {
            Optional<ActualCoin> actualCoin = actualCoinService.getById(userCoin.getCryptoId());
            BigDecimal actualPrice = actualCoin.get().getPrice();
            BigDecimal userPrice = userCoin.getPrice();

            if (userPrice.compareTo(BigDecimal.ZERO) == 0)
                continue;

            BigDecimal priceDiffPercent = actualPrice.divide(userPrice, 4, RoundingMode.HALF_UP)
                    .subtract(BigDecimal.ONE)
                    .multiply(BigDecimal.valueOf(100));


            log.info("Actual price for coin " + userCoin.getSymbol() + ": " + actualPrice + "\t" + "price of user " + userCoin.getUser().getUsername() + ": " + userPrice);

            if (priceDiffPercent.abs().compareTo(BigDecimal.ONE) > 0) {
                log.warn("Price change for coin " + userCoin.getSymbol() + " registered by user " +
                        userCoin.getUser().getUsername() + " is more than 1% (" +
                        priceDiffPercent.setScale(2, RoundingMode.HALF_UP) + "%).");
            }
        }
    }
}
