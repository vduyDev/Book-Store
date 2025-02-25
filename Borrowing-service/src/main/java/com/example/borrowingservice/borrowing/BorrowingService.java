package com.example.borrowingservice.borrowing;

import com.example.common.request.BookPurchaseRequest;
import com.example.common.response.ApiResponse;
import com.example.common.response.BorrowingResponse;
import com.example.common.response.PaymentResponse;
import com.example.common.request.BorrowingRequest;

import java.security.Principal;
import java.util.List;


public interface BorrowingService {


    PaymentResponse createBorrowing(BorrowingRequest borrowing, Principal principal);

    BorrowingResponse getBorrowingById(String id);

    void updateStatusBorrowingById(String id);

    List<BorrowingResponse> getListBorrowing();

    List<BorrowingResponse> getListBorrowingByCustomer(String id);

    BorrowingResponse bookRecord(String id,List<Integer> request);
}
