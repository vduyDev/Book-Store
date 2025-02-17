package com.example.paymentservice.payment;
import com.example.common.enums.PaymentMethod;
import com.example.common.enums.Status;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import java.time.Instant;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EntityListeners(AuditingEntityListener.class)
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private Long amount;
    @Enumerated(EnumType.STRING)
    private PaymentMethod paymentMethod;
    private String borrowingId;
    @Enumerated(EnumType.STRING)
    private Status status;
    @CreatedDate
    @Column(updatable = false, nullable = false)
    private Instant createdDate;

}
