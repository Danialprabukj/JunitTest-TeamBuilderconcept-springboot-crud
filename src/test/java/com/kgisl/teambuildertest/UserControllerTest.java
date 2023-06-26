package com.kgisl.teambuildertest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.kgisl.teambuildertest.controller.UserController;
import com.kgisl.teambuildertest.model.User;
import com.kgisl.teambuildertest.repository.UserRepository;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserControllerTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserController userController;

    private User defaultUser;
    private User updatedUser;

   @BeforeEach
    public void setup() {
        defaultUser = new User("John Doe", "john@example.com");
        updatedUser = new User("Jane Smith", "jane@example.com");
    }

    @Test
    public void testCreateUser() {
        when(userRepository.save(any(User.class))).thenReturn(defaultUser);

        ResponseEntity<User> responseEntity = userController.createUser(defaultUser);

        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
        assertEquals(defaultUser, responseEntity.getBody());

        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    public void testGetUser_Success() {
        Long userId = 1L;

        when(userRepository.findById(userId)).thenReturn(Optional.of(defaultUser));

        ResponseEntity<User> responseEntity = userController.getUser(userId);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(defaultUser, responseEntity.getBody());

        verify(userRepository, times(1)).findById(userId);
    }

    @Test
    public void testGetUser_NotFound() {
        Long userId = 1L;

        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        ResponseEntity<User> responseEntity = userController.getUser(userId);

        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
        assertEquals(null, responseEntity.getBody());

        verify(userRepository, times(1)).findById(userId);
    }

    @Test
    public void testUpdateUser_Success() {
        Long userId = 1L;

        when(userRepository.findById(userId)).thenReturn(Optional.of(defaultUser));
        when(userRepository.save(defaultUser)).thenReturn(updatedUser);

        ResponseEntity<User> responseEntity = userController.updateUser(userId, updatedUser);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(updatedUser, responseEntity.getBody());

        verify(userRepository, times(1)).findById(userId);
        verify(userRepository, times(1)).save(defaultUser);
    }

    @Test
    public void testUpdateUser_NotFound() {
        Long userId = 1L;

        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        ResponseEntity<User> responseEntity = userController.updateUser(userId, updatedUser);

        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
        assertEquals(null, responseEntity.getBody());

        verify(userRepository, times(1)).findById(userId);
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    public void testDeleteUser_Success() {
        Long userId = 1L;

        when(userRepository.findById(userId)).thenReturn(Optional.of(defaultUser));

        ResponseEntity<Void> responseEntity = userController.deleteUser(userId);

        assertEquals(HttpStatus.NO_CONTENT, responseEntity.getStatusCode());
        assertEquals(null, responseEntity.getBody());

        verify(userRepository, times(1)).findById(userId);
        verify(userRepository, times(1)).delete(defaultUser);
    }

    @Test
    public void testDeleteUser_NotFound() {
        Long userId = 1L;

        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        ResponseEntity<Void> responseEntity = userController.deleteUser(userId);

        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
        assertEquals(null, responseEntity.getBody());

        verify(userRepository, times(1)).findById(userId);
        verify(userRepository, never()).delete(any(User.class));
    }
}
