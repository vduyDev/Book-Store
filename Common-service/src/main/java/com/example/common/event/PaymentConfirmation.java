package com.example.common.event;
import com.example.common.enums.PaymentMethod;
import lombok.Builder;


@Builder
public record PaymentConfirmation(
        String borrowingId,
        Long amount,
        PaymentMethod paymentMethod,
        String customerFirstname,
        String customerLastname,
        String customerEmail
) {
}
