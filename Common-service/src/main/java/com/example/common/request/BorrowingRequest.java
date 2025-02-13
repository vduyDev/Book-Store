package com.example.common.request;

import com.example.common.enums.PaymentMethod;
import lombok.Data;

import java.util.List;

@Data
public class BorrowingRequest {
    private PaymentMethod paymentMethod;
    private List<BookPurchaseRequest> bookPurchase;
}
