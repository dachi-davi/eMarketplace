package com.example.emarketplace.dto;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;
import java.math.BigDecimal;

@Data
public class ItemRequest {
    private String name;
    private BigDecimal price;
    private String description;
    private MultipartFile photo;
}