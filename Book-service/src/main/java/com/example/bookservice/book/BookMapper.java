package com.example.bookservice.book;

import com.example.bookservice.category.Category;
import com.example.common.response.CategoryResponse;
import com.example.common.DTO.BookPurchaseDTO;
import com.example.common.request.BookRequest;
import com.example.common.response.BookResponse;

public class BookMapper {

    public static BookResponse toBookResponse(Book book) {
        return BookResponse.builder()
                .author(book.getAuthor())
                .stock(book.getStock())
                .description(book.getDescription())
                .category(
                        book.getCategory() != null ?
                                CategoryResponse.builder()
                                        .id(book.getCategory().getId())
                                        .name(book.getCategory().getName())
                                        .build()
                                : null
                )
                .urlImage(book.getImageUrl())
                .name(book.getName())
                .id(book.getId())
                .price(book.getPrice())
                .build();
    }

    public static Book toBook(BookRequest request, Category category) {
        return Book.builder()
                .author(request.getAuthor())
                .stock(request.getStock())
                .description(request.getDescription())
                .category(category)
                .name(request.getName())
                .build();
    }

    public static BookPurchaseDTO toBookPurchaseDTO(Book books) {
        return BookPurchaseDTO.builder()
                .bookId(books.getId())
                .name(books.getName())
                .price(books.getPrice())
                .image(books.getImageUrl())
                .build();
    }

}
