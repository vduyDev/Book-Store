package com.example.bookservice.category;

import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepo extends JpaRepository<Category,Integer> {
    Category getCategoryByName(String name);
}
