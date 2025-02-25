package com.example.borrowingservice.borrowing;

import com.example.common.request.BookPurchaseRequest;
import com.example.common.request.BookRecordRequest;
import com.example.common.response.BorrowingResponse;
import com.example.common.response.PaymentResponse;
import com.example.common.request.BorrowingRequest;
import com.example.common.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/borrowings")
@AllArgsConstructor
public class BorrowingController {

    private final BorrowingService borrowingService;


    @Operation(summary ="Đặt sách" )
    @PostMapping("/pur chase")
    public ApiResponse<PaymentResponse> bookPurchase(@RequestBody BorrowingRequest request, Principal principal) {

        return ApiResponse.<PaymentResponse>builder()
                .message("Book purchased successfully")
                .status(200)
                .data(borrowingService.createBorrowing(request, principal))
                .build();
    }

    @Operation(summary ="Lấy thông tin đặt sách theo id" )
    @GetMapping("/{id}")
    public ApiResponse<BorrowingResponse> getBorrowingById(@PathVariable String id){
        return  ApiResponse.<BorrowingResponse>builder()
                .message("successfully")
                .status(200)
                .data(borrowingService.getBorrowingById(id))
                .build();
    }

    @Operation(summary ="Lấy danh sách thông tin đặt sách" )
    @GetMapping
    public  ApiResponse<List<BorrowingResponse>> getListBorrowing(){
        return ApiResponse.<List<BorrowingResponse>>builder()
                .message("successfully")
                .status(200)
                .data(borrowingService.getListBorrowing())
                .build();
    }

    @Operation(summary ="Lấy danh sách thông tin đặt sách theo customer" )
    @GetMapping("/get-list-borrowing-by-customer/{id}")
    public ApiResponse<List<BorrowingResponse>> getListBorrowingByCustomer(@PathVariable String id){
        return  ApiResponse.<List<BorrowingResponse>>builder()
                .message("successfully")
                .status(200)
                .data(borrowingService.getListBorrowingByCustomer(id))
                .build();
    }

    @Operation(summary ="Trả sách" )
    @PostMapping("/book-record/{id}")
    public ApiResponse<BorrowingResponse> bookRecord(@PathVariable String id,@RequestBody List<Integer> request){
        return ApiResponse.<BorrowingResponse>builder()
                .message("successfully")
                .status(200)
                .data( borrowingService.bookRecord(id,request))
                .build();


    }
}
