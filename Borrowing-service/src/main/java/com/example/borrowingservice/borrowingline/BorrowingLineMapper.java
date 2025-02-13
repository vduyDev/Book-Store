package com.example.borrowingservice.borrowingline;

import com.example.common.DTO.BookPurchaseDTO;
import com.example.common.DTO.BorrowingLineDTO;
import com.example.common.enums.StatusBorrowing;
import com.example.common.request.BookPurchaseRequest;

import java.math.BigDecimal;

public class BorrowingLineMapper {
    public static BorrowingLine mapToBorrowingLine(BookPurchaseRequest bookPurchaseRequest) {
        return BorrowingLine.builder()
                .bookId(bookPurchaseRequest.getBookId())
                .quantity(bookPurchaseRequest.getQuantity())
                .dueDate(bookPurchaseRequest.getDueDate())
                .status(StatusBorrowing.ACTIVE)
                .fine(0L)
                .returnDate(null)
                .build();
    }

    public static BorrowingLineDTO toBorrowingLineDTO(BorrowingLine borrowingLine) {
        BookPurchaseDTO book = BookPurchaseDTO.builder()
                .bookId(borrowingLine.getBookId())
                .build();


        return BorrowingLineDTO.builder()
                .id(borrowingLine.getId())
                .book(book)
                .returnDate(borrowingLine.getReturnDate())
                .dueDate(borrowingLine.getDueDate())
                .status(borrowingLine.getStatus())
                .quantity(borrowingLine.getQuantity())
                .fine(borrowingLine.getFine())
                .build();
    }
}
