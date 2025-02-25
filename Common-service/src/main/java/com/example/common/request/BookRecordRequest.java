package com.example.common.request;

import lombok.Data;

import java.util.List;

@Data
public class BookRecordRequest {
    private List<BookPurchaseRequest> bookPurchase;
}
