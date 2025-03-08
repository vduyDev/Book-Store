package com.example.common.event;
import com.example.common.DTO.BookPurchaseDTO;
import com.example.common.DTO.CustomerDTO;
import com.example.common.enums.PaymentMethod;
import lombok.Builder;

import java.util.List;


@Builder
public record BorrowingConfirmation(
        String borrowingId,
        Long total,
        PaymentMethod paymentMethod,
        CustomerDTO customer,
        List<BookPurchaseDTO> books
){

}


