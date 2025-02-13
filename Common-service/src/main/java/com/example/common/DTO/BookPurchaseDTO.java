package com.example.common.DTO;


import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BookPurchaseDTO {
    private Integer bookId;
    private Long price;
    private String name;
    private Integer quantity;
    private String image;
}
