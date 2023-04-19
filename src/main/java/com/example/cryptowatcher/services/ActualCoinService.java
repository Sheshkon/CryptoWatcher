package com.example.cryptowatcher.services;

import com.example.cryptowatcher.models.ActualCoin;
import com.example.cryptowatcher.repositories.ActualCoinRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ActualCoinService {

    private final ActualCoinRepository actualCoinRepository;
    private final RestTemplate restTemplate;

    @Value("${crypto.api.url}")
    private String apiUrl;


    public List<ActualCoin> getAvailableCoins(){
        return actualCoinRepository.findAll();
    }

    public Optional<ActualCoin> getById(Long id){
        return actualCoinRepository.findById(id);
    }

    public Optional<ActualCoin> getBySymbol(String symbol){
        return actualCoinRepository.findBySymbol(symbol);
    }

    public void update() throws IOException {
        List<ActualCoin> coins = actualCoinRepository.findAll();

        for (ActualCoin coin : coins) {
            String url = apiUrl + "?id=" + coin.getId();
            String response = restTemplate.getForObject(url, String.class);

            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode root = objectMapper.readTree(response);
            String priceStr = root.get(0).get("price_usd").textValue().replaceAll("\"", "");
            BigDecimal currentPrice = new BigDecimal(priceStr);

            coin.setPrice(currentPrice);
            actualCoinRepository.save(coin);
        }
    }

}
