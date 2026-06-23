package com.example.emarketplace;

import com.example.emarketplace.dto.LoginRequest;
import com.example.emarketplace.repository.UserRepository;
import com.example.emarketplace.service.FileStorageService;
import com.example.emarketplace.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.test.context.bean.MockitoBean;
//import org.springframework.test.context.bean.MockitoSpyBean;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
class EMarketplaceIntegrationTest {

    @Autowired
    private UserService userService;

    @org.springframework.test.context.bean.override.mockito.MockitoBean
    private UserRepository userRepository;

    @org.springframework.test.context.bean.override.mockito.MockitoSpyBean
    private FileStorageService fileStorageService;

    @Test
    void login_ShouldThrowException_WhenUserDoesNotExistInSystem() {

        LoginRequest request = new LoginRequest();
        request.setIdentifier("unknownUser");
        request.setPassword("anyPassword");

        when(userRepository.findByUsername("unknownUser")).thenReturn(Optional.empty());
        when(userRepository.findByEmail("unknownUser")).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> {
            userService.login(request);
        });

        verify(userRepository).findByUsername("unknownUser");
        verify(userRepository).findByEmail("unknownUser");
    }
}
