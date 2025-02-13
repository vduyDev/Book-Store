package com.example.common.response;


import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Collections;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PageResponse<T> {
    int currenPage;
    int totalPage;
    int pageSize;
    int totalElement;
    @Builder.Default
    private List<T> data = Collections.emptyList();
}
