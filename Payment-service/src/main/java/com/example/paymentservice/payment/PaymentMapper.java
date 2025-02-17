package com.example.paymentservice.payment;


import com.example.common.DTO.PaymentDTO;

public class PaymentMapper {

    public static PaymentDTO toPaymentDTO(Payment payment){
        return PaymentDTO.builder()
                .id(payment.getId())
                .paymentMethod(payment.getPaymentMethod())
                .amount(payment.getAmount())
                .borrowingId(payment.getBorrowingId())
                .status(payment.getStatus())
                .createDate(payment.getCreatedDate())
                .build();
    }
}
