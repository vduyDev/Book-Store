package com.example.paymentservice.payment;

import com.example.common.DTO.BookPurchaseDTO;
import com.example.common.DTO.CustomerDTO;
import com.example.common.enums.Status;
import com.example.common.event.BorrowingConfirmation;
import com.example.common.event.PaymentConfirmation;
import com.example.common.request.PaymentRequest;
import com.example.common.response.PaymentResponse;
import com.example.common.enums.PaymentMethod;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Service
@AllArgsConstructor
public class PaymentProcessCash implements PaymentProcessService {
    private final PaymentProducer paymentProducer;
    private final PaymentRepository paymentRepository;

    @Override
    public boolean support(String paymentType) {
        return PaymentMethod.CASH.name().equalsIgnoreCase(paymentType);
    }

    @Override
    public PaymentResponse createPayment(PaymentRequest request) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        String customerJson = objectMapper.writeValueAsString(request.getCustomerDTO());
        success(request.getBorrowingId(), request.getAmount(), request.getPaymentMethod(), customerJson,request.getBookPurchaseDTOS());

        return PaymentResponse.builder()
                .message("Borrowing and payment successfully")
                .build();
    }

    @Override
    public void success(
            String borrowingId,
            Long amount,
            PaymentMethod method,
            String customerJson,
            List<BookPurchaseDTO> books
    ) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        CustomerDTO customerDTO = objectMapper.readValue(customerJson, CustomerDTO.class);

        Payment payment = Payment.builder()
                .paymentMethod(method)
                .amount(amount)
                .status(Status.SUCCESS)
                .borrowingId(borrowingId)
                .build();
        paymentRepository.save(payment);

        PaymentConfirmation paymentConfirmation = PaymentConfirmation.builder()
                .paymentMethod(PaymentMethod.CASH)
                .amount(amount)
                .borrowingId(borrowingId)
                .customerEmail(customerDTO.getEmail())
                .customerFirstname(customerDTO.getFirstName())
                .customerLastname(customerDTO.getLastName())
                .build();

        BorrowingConfirmation borrowingConfirmation = BorrowingConfirmation.builder()
                .paymentMethod(PaymentMethod.CASH)
                .borrowingId(borrowingId)
                .books(books)
                .customer(customerDTO)
                .total(amount)
                .build();
        paymentProducer.sendBorrowingConfirmation(borrowingConfirmation);
        paymentProducer.sendPaymentConfirmation(paymentConfirmation);

    }

    @Override
    public void fail(String borrowingId, Long amount, PaymentMethod method) {

    }


}

