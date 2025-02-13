package com.example.borrowingservice.borrowing;
import com.example.common.response.BorrowingResponse;
import com.example.common.response.PaymentResponse;
import com.example.common.request.BorrowingRequest;

import java.security.Principal;


public interface BorrowingService {
     PaymentResponse createBorrowing(BorrowingRequest borrowing, Principal principal);
     BorrowingResponse getBorrowingById(String id);
     void updateStatusBorrowingById(String id);
}
