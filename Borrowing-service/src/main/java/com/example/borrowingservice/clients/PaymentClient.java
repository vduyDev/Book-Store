package com.example.borrowingservice.clients;
import com.example.common.configure.FeignClientConfig;
import com.example.common.request.PaymentRequest;
import com.example.common.response.PaymentResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(name = "payment-service", configuration = FeignClientConfig.class)
public interface PaymentClient {

    @PostMapping("/payments/process")
    PaymentResponse process(PaymentRequest request);

}
