package com.example.paymentservice.payment;

import com.example.common.DTO.BookPurchaseDTO;
import com.example.common.DTO.CustomerDTO;
import com.example.common.enums.PaymentMethod;
import com.example.common.request.PaymentRequest;
import com.example.common.response.PaymentResponse;
import com.fasterxml.jackson.core.JsonProcessingException;

import java.util.List;

public interface PaymentProcessService {
    boolean support(String paymentType);
    PaymentResponse createPayment(PaymentRequest request) throws JsonProcessingException;
    void success(String borrowingId, Long amount, PaymentMethod method, String customerJson, List<BookPurchaseDTO> bookPurchaseDTOS) throws JsonProcessingException;
    void fail(String borrowingId, Long amount, PaymentMethod method);
}
