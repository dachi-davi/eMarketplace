package com.example.emarketplace.dto;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;
import java.math.BigDecimal;
import java.util.UUID;

@Data
public class ItemRequest {
    private String name;
    private BigDecimal price;
    private String description;
    private MultipartFile photo;
    private UUID userId;
}