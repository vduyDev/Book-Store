package com.example.common.request;


import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StockUpdateRequest {
    @Positive(message = "Số lượng phải lớn hơn 0")
    int stock;
}
