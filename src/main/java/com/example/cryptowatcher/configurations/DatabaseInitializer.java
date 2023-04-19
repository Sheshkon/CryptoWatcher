package com.example.cryptowatcher.configurations;

import com.example.cryptowatcher.models.ActualCoin;
import com.example.cryptowatcher.repositories.ActualCoinRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class DatabaseInitializer implements CommandLineRunner {

    private final ActualCoinRepository actualCoinRepository;
    private final Environment env;

    @Override
    public void run(String... args) throws Exception {
        String currenciesString = env.getProperty("crypto.api.currencies");
        ObjectMapper mapper = new ObjectMapper();
        List<ActualCoin> currencies = mapper.readValue(currenciesString, new TypeReference<>() {});

        for (ActualCoin currency : currencies) {
            Optional<ActualCoin> existingCurrency = actualCoinRepository.findBySymbol(currency.getSymbol());
            if (existingCurrency.isEmpty()) {
                actualCoinRepository.save(currency);
            }
        }
    }
}
