package com.example.borrowingservice.borrowing;

import com.example.borrowingservice.borrowingline.BorrowingLine;
import com.example.common.enums.StatusBorrowing;
import com.example.common.enums.PaymentMethod;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@EntityListeners(AuditingEntityListener.class)
public class Borrowing {
    @Id
    @Column(length = 5)
    String id;
    String customerId;
    @CreatedDate
    Instant borrowDate;
    Long totalAmount;
    Integer totalBook;
    @Enumerated(EnumType.STRING)
    StatusBorrowing status;
    @Enumerated(EnumType.STRING)
    PaymentMethod paymentMethod;
    @OneToMany(mappedBy = "borrowing")
    List<BorrowingLine> borrowingLines;
}
