package com.example.common.DTO;
import com.example.common.enums.PaymentMethod;
import com.example.common.enums.Status;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PaymentDTO {
    private Integer id;
    private String borrowingId;
    private PaymentMethod paymentMethod;
    private Long amount;
    private Instant createDate;
    private Status status;
}
