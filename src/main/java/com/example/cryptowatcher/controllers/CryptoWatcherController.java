package com.example.cryptowatcher.controllers;


import com.example.cryptowatcher.exceptions.CoinNotAvailableException;
import com.example.cryptowatcher.models.ActualCoin;
import com.example.cryptowatcher.models.UserCoin;
import com.example.cryptowatcher.services.ActualCoinService;
import com.example.cryptowatcher.services.UserCoinService;
import com.example.cryptowatcher.services.NotifyService;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;


@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class CryptoWatcherController {

    private final UserCoinService coinService;
    private final NotifyService notifyService;
    private final ActualCoinService actualCoinService;

    @GetMapping("/users-coins")
    public ResponseEntity<List<UserCoin>> getUserCoins() {
        List<UserCoin> userCoins = coinService.getCoins();
        return new ResponseEntity<>(userCoins, HttpStatus.OK);
    }

    @PostMapping("/notify")
    public ResponseEntity<String> notify(@RequestParam("username") String username, @RequestParam("symbol") String symbol) throws CoinNotAvailableException {
        String msg = notifyService.register(username, symbol);
        return ResponseEntity.ok(msg);
    }

    @GetMapping("/coins/symbol/{symbol}")
    public ResponseEntity<Optional<ActualCoin>> getCoinBySymbol(@PathVariable("symbol") String symbol) {
        Optional<ActualCoin> coin = actualCoinService.getBySymbol(symbol);
        return new ResponseEntity<>(coin, HttpStatus.OK);
    }

    @GetMapping("/coins/{id}")
    public ResponseEntity<Optional<ActualCoin>> getCoinById(@PathVariable("id") Long id) {
        Optional<ActualCoin> coin = actualCoinService.getById(id);
        return new ResponseEntity<>(coin, HttpStatus.OK);
    }

    @GetMapping("/coins")
    public ResponseEntity<List<ActualCoin>> getCoins() {
        List<ActualCoin> coins = actualCoinService.getAvailableCoins();
        return new ResponseEntity<>(coins, HttpStatus.OK);
    }

    @ExceptionHandler(CoinNotAvailableException.class)
    public ResponseEntity<String> handleCoinNotAvailableException(CoinNotAvailableException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("An error occurred: " + ex.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleUnexpectedException(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred: " + ex.getMessage());
    }
}
