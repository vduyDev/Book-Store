package com.example.borrowingservice.borrowingline;

import com.example.borrowingservice.borrowing.Borrowing;
import com.example.common.enums.StatusBorrowing;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.time.Instant;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BorrowingLine {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;
    Instant returnDate;
    Instant dueDate;
    Integer bookId;
    @Enumerated(EnumType.STRING)
    StatusBorrowing status;
    Integer quantity;
    @ManyToOne
    @JoinColumn(name = "borrowing_id")
    Borrowing borrowing;
}
