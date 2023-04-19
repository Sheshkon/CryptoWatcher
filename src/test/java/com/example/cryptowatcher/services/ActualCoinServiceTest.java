package com.example.cryptowatcher.services;

import com.example.cryptowatcher.models.ActualCoin;
import com.example.cryptowatcher.repositories.ActualCoinRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@SpringBootTest
@ActiveProfiles("test")
public class ActualCoinServiceTest {

    @Mock
    private ActualCoinRepository actualCoinRepository;

    @Mock
    private RestTemplate restTemplate;

    private ActualCoinService actualCoinService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        actualCoinService = new ActualCoinService(actualCoinRepository, restTemplate);
    }

    @Test
    void testGetAvailableCoins() {
        // Setup mock data
        List<ActualCoin> coins = new ArrayList<>();
        ActualCoin btc = new ActualCoin();
        btc.setId(1L);
        btc.setSymbol("BTC");
        btc.setPrice(new BigDecimal("60000"));
        ActualCoin eth = new ActualCoin();
        eth.setId(2L);
        eth.setSymbol("ETH");
        eth.setPrice(new BigDecimal("2000"));
        coins.add(btc);
        coins.add(eth);
        when(actualCoinRepository.findAll()).thenReturn(coins);

        // Call the method
        List<ActualCoin> result = actualCoinService.getAvailableCoins();

        // Verify the result
        assertEquals(2, result.size());
        assertEquals("BTC", result.get(0).getSymbol());
        assertEquals(new BigDecimal("60000"), result.get(0).getPrice());
        assertEquals("ETH", result.get(1).getSymbol());
        assertEquals(new BigDecimal("2000"), result.get(1).getPrice());
    }

    @Test
    void testGetById() {
        // Setup mock data
        ActualCoin coin = new ActualCoin();
        coin.setId(1L);
        coin.setSymbol("BTC");
        coin.setPrice(new BigDecimal("60000"));
        when(actualCoinRepository.findById(1L)).thenReturn(Optional.of(coin));

        // Call the method
        Optional<ActualCoin> result = actualCoinService.getById(1L);

        // Verify the result
        assertTrue(result.isPresent());
        assertEquals("BTC", result.get().getSymbol());
        assertEquals(new BigDecimal("60000"), result.get().getPrice());
    }

    @Test
    void testGetBySymbol() {
        // Setup mock data
        ActualCoin coin = new ActualCoin();
        coin.setId(1L);
        coin.setSymbol("BTC");
        coin.setPrice(new BigDecimal("60000"));
        when(actualCoinRepository.findBySymbol("BTC")).thenReturn(Optional.of(coin));

        // Call the method
        Optional<ActualCoin> result = actualCoinService.getBySymbol("BTC");

        // Verify the result
        assertTrue(result.isPresent());
        assertEquals(1L, result.get().getId());
        assertEquals(new BigDecimal("60000"), result.get().getPrice());
    }

    @Test
    void testUpdate() throws IOException {
        // Setup mock data
        List<ActualCoin> coins = new ArrayList<>();
        ActualCoin btc = new ActualCoin();
        btc.setId(1L);
        btc.setSymbol("BTC");
        btc.setPrice(new BigDecimal("60000"));
        ActualCoin eth = new ActualCoin();
        eth.setId(2L);
        eth.setSymbol("ETH");
        eth.setPrice(new BigDecimal("2000"));
        coins.add(btc);
        coins.add(eth);
        when(actualCoinRepository.findAll()).thenReturn(coins);

        String response = "[{\"id\":\"1\",\"name\":\"Bitcoin\",\"symbol\":\"BTC\",\"rank\":\"1\",\"price_usd\":\"78000\"}]";
        when(restTemplate.getForObject(anyString(), any())).thenReturn(response);

        // Call the method
        actualCoinService.update();

        // Verify the result
        assertEquals(new BigDecimal("78000"), btc.getPrice());
        assertEquals(new BigDecimal("78000"), eth.getPrice());
    }

}