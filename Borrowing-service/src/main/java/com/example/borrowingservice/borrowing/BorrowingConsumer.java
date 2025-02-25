package com.example.borrowingservice.borrowing;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
@Slf4j
public class BorrowingConsumer {

    private final BorrowingService borrowingService;

    @KafkaListener(topics = "payment-failed", groupId = "borrowing")
    public void updateStatusBorrowing(String borrowingId) {
        borrowingService.updateStatusBorrowingById(borrowingId);
       log.info("update borrowing {}", borrowingId);
    }


}
