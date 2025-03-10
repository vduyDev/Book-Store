package com.example.common.response;
import com.example.common.DTO.CustomerDTO;
import com.example.common.DTO.PaymentDTO;
import com.example.common.enums.StatusBorrowing;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BorrowingResponse {
    private String id;
    private CustomerDTO customer;
    private Long totalAmount;
    private Integer totalBook;
    private Instant borrowDate;
    private StatusBorrowing status;
    private PaymentDTO payment;

}
