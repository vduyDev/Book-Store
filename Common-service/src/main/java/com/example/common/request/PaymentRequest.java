package com.example.common.request;

import com.example.common.DTO.BookPurchaseDTO;
import com.example.common.DTO.CustomerDTO;
import com.example.common.enums.PaymentMethod;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PaymentRequest {
    private PaymentMethod paymentMethod;
    private Long amount;
    private String borrowingId;
    private List<BookPurchaseDTO> bookPurchaseDTOS;
    private CustomerDTO customerDTO;
}
