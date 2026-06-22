package com.example.emarketplace.dto;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class ItemResponse {
    private Long id;
    private String name;
    private BigDecimal price;
    private String description;
    private LocalDateTime submissionTime;
    private String photoUrl;
    private String username;
}