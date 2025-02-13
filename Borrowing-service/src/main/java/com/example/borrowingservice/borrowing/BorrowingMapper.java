package com.example.borrowingservice.borrowing;

import com.example.common.response.BorrowingResponse;

public class BorrowingMapper {
    // borrowing to BorrowingResponse
    public static BorrowingResponse toBorrowingResponse(Borrowing borrowing) {
        return BorrowingResponse.builder()
                .id(borrowing.getId())
                .borrowDate(borrowing.getBorrowDate())
                .totalAmount(borrowing.getTotalAmount())
                .totalBook(borrowing.getTotalBook())
                .totalFine(borrowing.getTotalFine())
                .status(borrowing.getStatus())
                .paymentMethod(borrowing.getPaymentMethod())
                .build();
    }
}
