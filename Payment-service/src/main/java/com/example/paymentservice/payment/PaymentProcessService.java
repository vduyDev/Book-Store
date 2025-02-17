package com.example.paymentservice.payment;

import com.example.common.enums.PaymentMethod;
import com.example.common.request.PaymentRequest;
import com.example.common.response.PaymentResponse;

public interface PaymentProcessService {
    boolean support(String paymentType);
    PaymentResponse createPayment(PaymentRequest request);
    void success(String borrowingId, Long amount, PaymentMethod method);
    void fail(String borrowingId, Long amount, PaymentMethod method);
}
