package com.example.cryptowatcher.services;

import com.example.cryptowatcher.models.User;
import com.example.cryptowatcher.repositories.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import java.util.Optional;

@SpringBootTest
public class UserServiceTest {
    @Mock
    private UserRepository userRepositoryMock;

    @Test
    public void createUser_shouldSaveUserToRepository() {
        UserService userService = new UserService(userRepositoryMock);
        String username = "testuser";
        User user = new User();
        user.setUsername(username);

        Mockito.when(userRepositoryMock.save(user)).thenReturn(user);

        User createdUser = userService.createUser(username);

        Mockito.verify(userRepositoryMock, Mockito.times(1)).save(user);
        Assertions.assertEquals(username, createdUser.getUsername());
    }

    @Test
    public void getUserByUsername_shouldReturnUserIfExists() {
        UserService userService = new UserService(userRepositoryMock);
        String username = "testuser";
        User user = new User();
        user.setUsername(username);

        Mockito.when(userRepositoryMock.findByUsername(username)).thenReturn(Optional.of(user));

        Optional<User> retrievedUser = userService.getUserByUsername(username);

        Mockito.verify(userRepositoryMock, Mockito.times(1)).findByUsername(username);
        Assertions.assertTrue(retrievedUser.isPresent());
        Assertions.assertEquals(username, retrievedUser.get().getUsername());
    }

    @Test
    public void getUserByUsername_shouldReturnEmptyOptionalIfUserDoesNotExist() {
        UserService userService = new UserService(userRepositoryMock);
        String username = "testuser";

        Mockito.when(userRepositoryMock.findByUsername(username)).thenReturn(Optional.empty());

        Optional<User> retrievedUser = userService.getUserByUsername(username);

        Mockito.verify(userRepositoryMock, Mockito.times(1)).findByUsername(username);
        Assertions.assertFalse(retrievedUser.isPresent());
    }
}