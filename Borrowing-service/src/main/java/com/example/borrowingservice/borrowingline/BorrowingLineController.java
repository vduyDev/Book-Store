package com.example.borrowingservice.borrowingline;

import com.example.common.DTO.BorrowingLineDTO;
import com.example.common.response.ApiResponse;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/borrowinglines")
@AllArgsConstructor
public class BorrowingLineController {

    private final BorrowingLineService borrowingLineService;

    @GetMapping("/get-by-borrowing/{borrowingId}")
    public ApiResponse<List<BorrowingLineDTO>> getBorrowingLinesByBorrowingId(@PathVariable String borrowingId) {
        System.out.println("borrowing");
        return ApiResponse.<List<BorrowingLineDTO>>builder()
                .status(200)
                .message("Success")
                .data(borrowingLineService.getBorrowingLines(borrowingId))
                .build();
    }
}
