package com.example.borrowingservice.borrowing;

import com.example.common.response.PaymentResponse;
import com.example.common.request.BorrowingRequest;
import com.example.common.response.ApiResponse;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/borrowings")
@AllArgsConstructor
public class BorrowingController {

    private final BorrowingService borrowingService;

    @PostMapping("/purchase")
    public ApiResponse<PaymentResponse> bookPurchase(@RequestBody BorrowingRequest request, Principal principal) {

        return ApiResponse.<PaymentResponse>builder()
                .message("Book purchased successfully")
                .status(200)
                .data(borrowingService.createBorrowing(request, principal))
                .build();
    }

}
