
package com.example.paymentservice.payment;

import com.example.common.enums.PaymentMethod;
import com.example.common.enums.Status;
import com.example.common.request.PaymentRequest;
import com.example.common.response.PaymentResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/payments")
@Slf4j
public class PaymentController {

    @Autowired
    private List<PaymentService> paymentService;

    @Autowired
    private PaymentProducer paymentProducer;

    @PostMapping("/process")
    public PaymentResponse process(@RequestBody PaymentRequest request) {

        return paymentService.stream()
                .filter(paymentService -> paymentService.support(request.getPaymentMethod().name()))
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
        paymentService.stream()
                .filter(paymentService -> paymentService.support(method.name()))
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
        paymentService.stream()
                .filter(paymentService -> paymentService.support(method.name()))
                .findFirst()
                .orElseThrow()
                .fail(borrowingId, amount, method);

        return PaymentResponse
                .builder()
                .message("Payment cancel")
                .build();
    }

    @GetMapping("/test/{message}")
    public void test(@PathVariable String message) {
        paymentProducer.sendMessage(message);
        log.info("Send message: {}", message);
    }
}
