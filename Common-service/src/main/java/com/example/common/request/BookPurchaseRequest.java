package com.example.common.request;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BookPurchaseRequest {
    @NotNull(message = "Book ID is required")
    private Integer bookId;
    @Positive(message = "Quantity must be greater than 0")
    private int quantity;
    private Instant dueDate;
}
