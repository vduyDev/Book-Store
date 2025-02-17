package com.example.borrowingservice.borrowing;

import com.example.common.response.BorrowingResponse;
import com.example.common.response.PaymentResponse;
import com.example.common.request.BorrowingRequest;
import com.example.common.response.ApiResponse;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

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

    @GetMapping("/{id}")
    public ApiResponse<BorrowingResponse> getBorrowingById(@PathVariable String id){
        return  ApiResponse.<BorrowingResponse>builder()
                .message("successfully")
                .status(200)
                .data(borrowingService.getBorrowingById(id))
                .build();
    }

    @GetMapping
    public  ApiResponse<List<BorrowingResponse>> getListBorrowing(){
        return ApiResponse.<List<BorrowingResponse>>builder()
                .message("successfully")
                .status(200)
                .data(borrowingService.getListBorrowing())
                .build();
    }
}
