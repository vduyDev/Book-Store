package com.example.borrowingservice.borrowing;

import com.example.common.DTO.CustomerDTO;
import com.example.common.DTO.PaymentDTO;
import com.example.common.response.BorrowingResponse;

public class BorrowingMapper {
    // borrowing to BorrowingResponse
    public static BorrowingResponse toBorrowingResponse(
            Borrowing borrowing,
            CustomerDTO customerDTO,
            PaymentDTO paymentDTO
    ) {
        return BorrowingResponse.builder()
                .id(borrowing.getId())
                .borrowDate(borrowing.getBorrowDate())
                .totalAmount(borrowing.getTotalAmount())
                .totalBook(borrowing.getTotalBook())
                .status(borrowing.getStatus())
                .payment(paymentDTO)
                .customer(customerDTO)
                .build();
    }
}
