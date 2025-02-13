package com.example.paymentservice.payment;

import com.example.common.request.PaymentRequest;
import com.example.common.response.PaymentResponse;
import com.example.common.enums.PaymentMethod;
import com.example.common.enums.Status;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class PaymentCash implements PaymentService {

    private final PaymentRepository paymentRepository;

    @Override
    public boolean support(String paymentType) {
        return PaymentMethod.CASH.name().equalsIgnoreCase(paymentType);
    }

    @Override
    public PaymentResponse createPayment(PaymentRequest request) {


        Payment payment = Payment.builder()
                .paymentMethod(request.getPaymentMethod())
                .amount(request.getAmount())
                .borrowingId(request.getBorrowingId())
                .build();
        paymentRepository.save(payment);

        return PaymentResponse.builder()
                .message("Borrowing and payment successfully")
                .build();
    }

    @Override
    public void success(String borrowingId, Long amount, PaymentMethod method) {

    }

    @Override
    public void fail(String borrowingId, Long amount, PaymentMethod method) {

    }


}

