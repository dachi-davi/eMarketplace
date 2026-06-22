package com.example.emarketplace.service;

import com.example.emarketplace.dto.ItemRequest;
import com.example.emarketplace.dto.ItemResponse;
import com.example.emarketplace.entity.Item;
import com.example.emarketplace.entity.User;
import com.example.emarketplace.repository.ItemRepository;
import com.example.emarketplace.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import java.io.IOException;

@Service
public class ItemService {

    private final ItemRepository itemRepository;
    private final FileStorageService fileStorageService;
    private final UserRepository userRepository;

    public ItemService(ItemRepository itemRepository, FileStorageService fileStorageService, UserRepository userRepository) {
        this.itemRepository = itemRepository;
        this.fileStorageService = fileStorageService;
        this.userRepository = userRepository;
    }

    public ItemResponse createItem(ItemRequest request) throws java.io.IOException {
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        String photoUrl = fileStorageService.saveFile(request.getPhoto());
        Item item = new Item();
        item.setName(request.getName());
        item.setPrice(request.getPrice());
        item.setDescription(request.getDescription());
        item.setPhotoUrl(photoUrl);
        item.setUser(user);

        return mapToResponse(itemRepository.save(item));
    }

    public org.springframework.data.domain.Page<ItemResponse> getItems(int page, int size, String sortParam) {
        org.springframework.data.domain.Sort sort;
        switch (sortParam) {
            case "dateAsc": sort = org.springframework.data.domain.Sort.by("submissionTime").ascending(); break;
            case "priceAsc": sort = org.springframework.data.domain.Sort.by("price").ascending(); break;
            case "priceDesc": sort = org.springframework.data.domain.Sort.by("price").descending(); break;
            case "dateDesc":
            default: sort = org.springframework.data.domain.Sort.by("submissionTime").descending(); break;
        }

        org.springframework.data.domain.PageRequest pr = org.springframework.data.domain.PageRequest.of(page, size, sort);
        return itemRepository.findAll(pr).map(this::mapToResponse);
    }

    public ItemResponse getItemById(Long id) {
        Item item = itemRepository.findById(id).orElseThrow(() -> new RuntimeException("Item not found"));
        return mapToResponse(item);
    }

    private ItemResponse mapToResponse(Item item) {
        ItemResponse response = new ItemResponse();
        response.setId(item.getId());
        response.setName(item.getName());
        response.setPrice(item.getPrice());
        response.setDescription(item.getDescription());
        response.setSubmissionTime(item.getSubmissionTime());
        response.setPhotoUrl(item.getPhotoUrl());
        response.setUsername(item.getUser().getUsername());
        return response;
    }
}