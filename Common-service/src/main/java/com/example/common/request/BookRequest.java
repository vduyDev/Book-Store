package com.example.common.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class BookRequest {
    @NotBlank(message = "Tên không được bỏ trống.")
    private String name;
    private String description;
    private String author;
    @Positive(message = "Số lượng phải lớn hơn 0.")
    private Integer stock;
    @NotNull(message = "Category là bắt buộc.")
    private Integer categoryId;
}
