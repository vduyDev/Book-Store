package com.example.common.DTO;

import com.example.common.enums.StatusBorrowing;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BorrowingLineDTO {
    Integer id;
    Instant returnDate;
    Instant dueDate;
    BookPurchaseDTO book;
    StatusBorrowing status;
    Integer quantity;
    Long fine;
}
