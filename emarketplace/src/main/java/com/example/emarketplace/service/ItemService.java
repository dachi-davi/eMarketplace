package com.example.emarketplace.service;

import com.example.emarketplace.dto.ItemRequest;
import com.example.emarketplace.dto.ItemResponse;
import com.example.emarketplace.entity.Item;
import com.example.emarketplace.repository.ItemRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import java.io.IOException;

@Service
public class ItemService {

    private final ItemRepository itemRepository;
    private final FileStorageService fileStorageService;

    public ItemService(ItemRepository itemRepository, FileStorageService fileStorageService) {
        this.itemRepository = itemRepository;
        this.fileStorageService = fileStorageService;
    }

    public ItemResponse createItem(ItemRequest request) throws IOException {
        String photoUrl = fileStorageService.saveFile(request.getPhoto());

        Item item = new Item();
        item.setName(request.getName());
        item.setPrice(request.getPrice());
        item.setDescription(request.getDescription());
        item.setPhotoUrl(photoUrl);

        Item savedItem = itemRepository.save(item);
        return mapToResponse(savedItem);
    }

    public Page<ItemResponse> getItems(int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "submissionTime"));
        return itemRepository.findAll(pageRequest).map(this::mapToResponse);
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
        return response;
    }
}