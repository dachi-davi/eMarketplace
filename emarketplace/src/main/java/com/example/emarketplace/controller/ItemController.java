package com.example.emarketplace.controller;

import com.example.emarketplace.dto.ItemRequest;
import com.example.emarketplace.dto.ItemResponse;
import com.example.emarketplace.service.ItemService;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.io.IOException;

@RestController
@RequestMapping("/items")
@CrossOrigin(origins = "*")
public class ItemController {

    private final ItemService itemService;

    public ItemController(ItemService itemService) {
        this.itemService = itemService;
    }

    @GetMapping
    public ResponseEntity<Page<ItemResponse>> getItems(
            @RequestParam(defaultValue = "0") int page) {
        return ResponseEntity.ok(itemService.getItems(page, 6));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ItemResponse> getItem(@PathVariable Long id) {
        return ResponseEntity.ok(itemService.getItemById(id));
    }

    @PostMapping
    public ResponseEntity<ItemResponse> createItem(@ModelAttribute ItemRequest request) throws IOException {
        return ResponseEntity.ok(itemService.createItem(request));
    }
}