package com.example.notificationservice.notification;
import com.example.common.enums.NotificationType;
import com.example.common.event.BorrowingConfirmation;
import com.example.common.event.PaymentConfirmation;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.LocalDateTime;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Document(collection = "notification")
public class Notification {

    @Id
    private String id;
    private NotificationType type;
    private LocalDateTime notificationDate;
    private PaymentConfirmation paymentConfirmation;
    private BorrowingConfirmation borrowingConfirmation;
}
