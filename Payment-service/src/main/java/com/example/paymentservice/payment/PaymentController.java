
package com.example.paymentservice.payment;

import com.example.common.DTO.PaymentDTO;
import com.example.common.enums.PaymentMethod;
import com.example.common.request.PaymentRequest;
import com.example.common.response.PaymentResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/payments")
@Slf4j
public class PaymentController {

    @Autowired
    private List<PaymentProcessService> paymentProcessServices;

    @Autowired
    private PaymentService paymentService;


    @PostMapping("/process")
    public PaymentResponse process(@RequestBody PaymentRequest request) {

        return paymentProcessServices.stream()
                .filter(paymentProcessService -> paymentProcessService.support(request.getPaymentMethod().name()))
                .findFirst()
                .orElseThrow()
                .createPayment(request);
    }


    @GetMapping("/success")
    public PaymentResponse success(
            @RequestParam("borrowingId") String borrowingId,
            @RequestParam("amount") Long amount,
            @RequestParam("method") PaymentMethod method
    ) {
        paymentProcessServices.stream()
                .filter(paymentProcessService -> paymentProcessService.support(method.name()))
                .findFirst()
                .orElseThrow()
                .success(borrowingId, amount, method);


        return PaymentResponse
                .builder()
                .message("Payment success")
                .build();
    }

    @GetMapping("/cancel")
    public PaymentResponse cancel(
            @RequestParam("borrowingId") String borrowingId,
            @RequestParam("method") PaymentMethod method,
            @RequestParam("amount") Long amount
    ) {
        paymentProcessServices.stream()
                .filter(paymentProcessService -> paymentProcessService.support(method.name()))
                .findFirst()
                .orElseThrow()
                .fail(borrowingId, amount, method);

        return PaymentResponse
                .builder()
                .message("Payment cancel")
                .build();
    }

    @GetMapping("/get-payment-by-borrowing-id/{id}")
    public PaymentDTO getPaymentByBorrowingId(@PathVariable String id) {
        return paymentService.getPaymentByBorrowingId(id);
    }

    @GetMapping
    public List<PaymentDTO> getListPayment() {
        return paymentService.getListPayment();
    }
}
