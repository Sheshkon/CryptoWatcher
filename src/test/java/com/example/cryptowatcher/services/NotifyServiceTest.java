package com.example.cryptowatcher.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

import java.math.BigDecimal;
import java.util.Optional;

import com.example.cryptowatcher.exceptions.CoinNotAvailableException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import com.example.cryptowatcher.models.ActualCoin;
import com.example.cryptowatcher.models.User;
import com.example.cryptowatcher.models.UserCoin;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
public class NotifyServiceTest {

    @Mock
    private UserCoinService userCoinService;

    @Mock
    private ActualCoinService actualCoinService;

    @Mock
    private UserService userService;

    @InjectMocks
    private NotifyService notifyService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testRegisterUserCoinCreated() throws CoinNotAvailableException {
        // arrange
        String username = "john";
        String symbol = "BTC";
        User user = new User();
        user.setUsername(username);
        Optional<User> userOptional = Optional.of(user);
        ActualCoin actualCoin = new ActualCoin();
        actualCoin.setId(1L);
        actualCoin.setSymbol(symbol);
        actualCoin.setPrice(BigDecimal.valueOf(50000));
        Optional<ActualCoin> actualCoinOptional = Optional.of(actualCoin);
        doReturn(userOptional).when(userService).getUserByUsername(username);
        doReturn(actualCoinOptional).when(actualCoinService).getBySymbol(symbol);
        doReturn(Optional.empty()).when(userCoinService).getByUserUsernameAndSymbol(username, symbol);
        doReturn(new UserCoin()).when(userCoinService).createUserCoin(user, actualCoin.getId(), symbol, actualCoin.getPrice());

        // act
        String result = notifyService.register(username, symbol);

        // assert
        assertEquals("User coin created", result);
    }

    @Test
    public void testRegisterUserCoinUpdated() throws CoinNotAvailableException {
        // arrange
        String username = "john";
        String symbol = "BTC";
        User user = new User();
        user.setUsername(username);
        Optional<User> userOptional = Optional.of(user);
        ActualCoin actualCoin = new ActualCoin();
        actualCoin.setId(1L);
        actualCoin.setSymbol(symbol);
        actualCoin.setPrice(BigDecimal.valueOf(50000));
        Optional<ActualCoin> actualCoinOptional = Optional.of(actualCoin);
        UserCoin userCoin = new UserCoin();
        userCoin.setCryptoId(actualCoin.getId());
        userCoin.setSymbol(symbol);
        userCoin.setPrice(BigDecimal.valueOf(40000));
        Optional<UserCoin> userCoinOptional = Optional.of(userCoin);
        doReturn(userOptional).when(userService).getUserByUsername(username);
        doReturn(actualCoinOptional).when(actualCoinService).getBySymbol(symbol);
        doReturn(userCoinOptional).when(userCoinService).getByUserUsernameAndSymbol(username, symbol);
        doReturn(userCoin).when(userCoinService).updateUserCoin(userCoin);

        // act
        String result = notifyService.register(username, symbol);

        // assert
        assertEquals("User coin updated", result);
    }

    @Test
    void testRegisterCoinNotAvailable() throws CoinNotAvailableException {
        String username = "testUser";
        String symbol = "notAvailableCoin";
        User user = new User();
        user.setId(1L);
        user.setUsername(username);

        Mockito.when(userService.getUserByUsername(username)).thenReturn(java.util.Optional.of(user));
        Mockito.when(actualCoinService.getBySymbol(symbol)).thenReturn(java.util.Optional.empty());

        Exception exception = assertThrows(Exception.class, () -> notifyService.register(username, symbol));

        String expectedMessage = "Coin with symbol " + symbol + " is not available.";
        String actualMessage = exception.getMessage();

        assertEquals(expectedMessage, actualMessage);
        Mockito.verify(userService, Mockito.times(1)).getUserByUsername(username);
        Mockito.verify(actualCoinService, Mockito.times(1)).getBySymbol(symbol);
        Mockito.verify(userCoinService, Mockito.times(0)).getByUserUsernameAndSymbol(username, symbol);
        Mockito.verify(userService, Mockito.times(0)).createUser(username);
        Mockito.verify(userCoinService, Mockito.times(0)).createUserCoin(user, null, symbol, null);
    }

}