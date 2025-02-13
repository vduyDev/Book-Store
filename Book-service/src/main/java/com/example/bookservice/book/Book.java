package com.example.bookservice.book;


import com.example.bookservice.category.Category;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String name;
    private String description;
    private String author;
    private Integer stock;
    private String imageUrl;
    private Long price;
    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

}
