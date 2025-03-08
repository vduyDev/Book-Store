
package com.example.paymentservice.payment;

import com.example.common.DTO.CustomerDTO;
import com.example.common.DTO.PaymentDTO;
import com.example.common.enums.PaymentMethod;
import com.example.common.request.PaymentRequest;
import com.example.common.response.PaymentResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.Operation;
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


    @Operation(summary = "Thanh toán")
    @PostMapping("/process")
    public PaymentResponse process(@RequestBody PaymentRequest request) throws JsonProcessingException {

        return paymentProcessServices.stream()
                .filter(paymentProcessService -> paymentProcessService.support(request.getPaymentMethod().name()))
                .findFirst()
                .orElseThrow()
                .createPayment(request);
    }

    @Operation(summary = "Thanh toán thành công( chỉ áp dụng cho Stripe )")
    @GetMapping("/success")
    public PaymentResponse success(
            @RequestParam("borrowingId") String borrowingId,
            @RequestParam("amount") Long amount,
            @RequestParam("method") PaymentMethod method,
            @RequestParam("customer") String customerJson
    ) throws JsonProcessingException {
        paymentProcessServices.stream()
                .filter(paymentProcessService -> paymentProcessService.support(method.name()))
                .findFirst()
                .orElseThrow()
                .success(borrowingId, amount, method,customerJson,null);

        return PaymentResponse
                .builder()
                .message("Payment success")
                .build();
    }

    @Operation(summary = "Thanh toán thất bại( chỉ áp dụng cho Stripe )")
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

    @Operation(summary = "Lấy thông tin thanh toán theo id theo thông tin đăt sách")
    @GetMapping("/get-payment-by-borrowing-id/{id}")
    public PaymentDTO getPaymentByBorrowingId(@PathVariable String id) {
        return paymentService.getPaymentByBorrowingId(id);
    }


    @Operation(summary = "Lấy danh sách thanh toán")
    @GetMapping
    public List<PaymentDTO> getListPayment() {
        return paymentService.getListPayment();
    }


    @Operation(summary = "Lấy danh sách thanh toán theo thông tin đặt sách")
    @PostMapping("/get-payment-by-list-borrowing")
    List<PaymentDTO> getPaymentByListBorrowing(@RequestBody List<String> ids){
        return  paymentService.getPaymentByListBorrowing(ids);
    }
}
