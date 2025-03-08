package com.example.notificationservice.notification;

import com.example.common.enums.NotificationType;
import com.example.common.event.BorrowingConfirmation;
import com.example.common.event.PaymentConfirmation;
import com.example.notificationservice.email.EmailService;
import jakarta.mail.MessagingException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@AllArgsConstructor
@Slf4j
public class NotificationConsumer {

    private NotificationRepository repository;
    private EmailService emailService;

    @KafkaListener(topics = "payment-success")
    public void consumePaymentSuccessNofitication(PaymentConfirmation paymentConfirmation) throws MessagingException {
        log.info("the message from payment-success: {}", paymentConfirmation);
            Notification notification = Notification.builder()
                    .paymentConfirmation(paymentConfirmation)
                    .notificationDate(LocalDateTime.now())
                    .type(NotificationType.PAYMENT_CONFIRMATION)
                    .build();
        repository.save(notification);
        var customerName = paymentConfirmation.customerFirstname() + " " + paymentConfirmation.customerLastname();
        emailService.sendPaymentSuccessEmail(
                paymentConfirmation.customerEmail(),
                customerName,
                paymentConfirmation.amount(),
                paymentConfirmation.borrowingId()
        );

    }

    @KafkaListener(topics = "borrowing-success")
    public void consumeBorrowingSuccessNofitication(BorrowingConfirmation confirmation) throws MessagingException {
        log.info("the message from borrowing-success: {}", confirmation);
        Notification notification = Notification.builder()
                .borrowingConfirmation(confirmation)
                .notificationDate(LocalDateTime.now())
                .type(NotificationType.BORROWING_CONFIRMATION)
                .build();
        repository.save(notification);

        var customerName = confirmation.customer().getFirstName() + " " + confirmation.customer().getLastName();
        emailService.sendBorrowingSuccessEmail(
                confirmation.customer().getEmail(),
                customerName,
                confirmation.total(),
                confirmation.borrowingId(),
                confirmation.books()
        );
    }
}
