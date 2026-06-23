package com.example.emarketplace.service;

import com.example.emarketplace.dto.ItemRequest;
import com.example.emarketplace.dto.ItemResponse;
import com.example.emarketplace.entity.Item;
import com.example.emarketplace.entity.User;
import com.example.emarketplace.repository.ItemRepository;
import com.example.emarketplace.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ItemServiceTest {

    @Mock
    private ItemRepository itemRepository;

    @Mock
    private UserRepository userRepository;

    @Spy
    private FileStorageService fileStorageService = new FileStorageService();

    @InjectMocks
    private ItemService itemService;

    @Test
    void createItem_ShouldMapAndSaveItemSuccessfully() throws IOException {
        UUID userId = UUID.randomUUID();
        User mockUser = new User();
        mockUser.setId(userId);
        mockUser.setUsername("sellerUsername");

        ItemRequest request = new ItemRequest();
        request.setName("Vintage Camera");
        request.setPrice(BigDecimal.valueOf(150.0));
        request.setDescription("Functional retro film camera.");
        request.setUserId(userId);
        request.setPhoto(new MockMultipartFile("photo", "camera.jpg", "image/jpeg", new byte[]{24}));

        Item mockSavedItem = new Item();
        mockSavedItem.setId(1L);
        mockSavedItem.setName("Vintage Camera");
        mockSavedItem.setPrice(BigDecimal.valueOf(150.0));
        mockSavedItem.setUser(mockUser);

        when(userRepository.findById(userId)).thenReturn(Optional.of(mockUser));

        doReturn("/uploads/camera.jpg").when(fileStorageService).saveFile(any());
        when(itemRepository.save(any(Item.class))).thenReturn(mockSavedItem);

        ItemResponse response = itemService.createItem(request);

        assertNotNull(response);
        assertEquals("Vintage Camera", response.getName());
        assertEquals("sellerUsername", response.getUsername());
        assertNull(response.getDescription());

        verify(userRepository, times(1)).findById(userId);
        verify(fileStorageService, times(1)).saveFile(any());
        verify(itemRepository, times(1)).save(any(Item.class));
    }
}