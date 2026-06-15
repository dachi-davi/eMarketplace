package com.example.emarketplace.repository;

import com.example.emarketplace.entity.Item;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItemRepository extends JpaRepository<Item, Long> {
}