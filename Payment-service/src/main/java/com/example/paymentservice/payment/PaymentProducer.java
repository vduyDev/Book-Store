package com.example.paymentservice.payment;

import com.example.common.event.BorrowingConfirmation;
import com.example.common.event.PaymentConfirmation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@Slf4j
public class PaymentProducer {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public void sendMessage(String message) {
        kafkaTemplate.send("payment-failed", message);
    }


    public void sendPaymentConfirmation(PaymentConfirmation paymentConfirmation) {
        kafkaTemplate.send("payment-success", paymentConfirmation);
        log.info("send message to payment-success topic ");
    }
    public void sendBorrowingConfirmation(BorrowingConfirmation borrowingConfirmation){
        kafkaTemplate.send("borrowing-success", borrowingConfirmation);
        log.info("send message to borrowing-success topic ");
    }
}


