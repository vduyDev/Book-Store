package com.example.borrowingservice.clients;
import com.example.common.DTO.PaymentDTO;
import com.example.common.configure.FeignClientConfig;
import com.example.common.request.PaymentRequest;
import com.example.common.response.PaymentResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient(name = "payment-service", configuration = FeignClientConfig.class)
public interface PaymentClient {

    @PostMapping("/payments/process")
    PaymentResponse process(PaymentRequest request);

    @GetMapping("/payments/get-payment-by-borrowing-id/{id}")
    PaymentDTO getPaymentByBorrowingId(@PathVariable String id);

    @GetMapping("/payments")
    List<PaymentDTO> getListPayment();


    @PostMapping("/payments/get-payment-by-list-borrowing")
    List<PaymentDTO> getPaymentByListBorrowing(@RequestBody List<String> ids);
}
