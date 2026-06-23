package com.example.emarketplace.service;

import com.example.emarketplace.dto.LoginRequest;
import com.example.emarketplace.dto.RegisterRequest;
import com.example.emarketplace.dto.UserResponse;
import com.example.emarketplace.entity.User;
import com.example.emarketplace.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @Test
    void register_ShouldReturnUserResponse_WhenDataIsValid() {
        RegisterRequest request = new RegisterRequest();
        request.setUsername("validUser123");
        request.setEmail("test@example.com");
        request.setPassword("securePassword");
        request.setBirthday(LocalDate.now().minusYears(20)); // Well over 13

        User mockSavedUser = new User();
        mockSavedUser.setId(UUID.randomUUID());
        mockSavedUser.setUsername("validUser123");

        when(userRepository.save(any(User.class))).thenReturn(mockSavedUser);

        UserResponse response = userService.register(request);

        assertNotNull(response);
        assertEquals("validUser123", response.getUsername());
        assertNotNull(response.getId());

        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void login_ShouldThrowException_WhenDatabaseAccessFails() {
        LoginRequest request = new LoginRequest();
        request.setIdentifier("adminUser");
        request.setPassword("password123");

        when(userRepository.findByUsername("adminUser"))
                .thenThrow(new RuntimeException("Database connection timed out"));

        Exception exception = assertThrows(RuntimeException.class, () -> {
            userService.login(request);
        });

        assertTrue(exception.getMessage().contains("Database connection timed out"));

        verify(userRepository, times(1)).findByUsername("adminUser");
        verify(userRepository, never()).findByEmail(anyString()); // Verifies email check was bypassed on failure
    }
}
