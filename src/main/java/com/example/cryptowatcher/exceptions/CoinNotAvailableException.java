package com.example.cryptowatcher.exceptions;

public class CoinNotAvailableException extends RuntimeException {

    public CoinNotAvailableException(String symbol) {
        super("Coin with symbol " + symbol + " is not available.");
    }

}