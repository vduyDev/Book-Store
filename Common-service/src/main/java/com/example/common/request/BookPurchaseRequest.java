package com.example.common.request;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.time.Instant;


@Data
public class BookPurchaseRequest {
    @NotNull(message = "Book ID is required")
    private Integer bookId;
    @Positive(message = "Quantity must be greater than 0")
    private int quantity;
    private Instant dueDate;
}
