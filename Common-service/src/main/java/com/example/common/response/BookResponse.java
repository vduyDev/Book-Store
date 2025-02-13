package com.example.common.response;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BookResponse {
    private Integer id;
    private String name;
    private Long price;
    private String description;
    private String author;
    private Integer stock;
    private String urlImage;
    private CategoryResponse category;

}
