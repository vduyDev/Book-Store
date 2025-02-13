package com.example.paymentservice.payment;
import com.example.common.request.PaymentRequest;
import com.example.common.response.PaymentResponse;
import com.example.common.enums.PaymentMethod;
import com.example.paymentservice.config.VNPAYConfig;
import com.example.paymentservice.utils.VNPayUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@AllArgsConstructor
public class PaymentVNPay implements PaymentService {
    private final VNPAYConfig vnPayConfig;

    @Override
    public boolean support(String paymentType) {
        return PaymentMethod.VN_PAY.name().equals(paymentType);
    }

    @Override
    public PaymentResponse createPayment(PaymentRequest request) {
//        long amount = Integer.parseInt(request.getParameter("amount")) * 100L;
//
//        String bankCode = request.getParameter("bankCode");
//        Map<String, String> vnpParamsMap = vnPayConfig.getVNPayConfig();
//        vnpParamsMap.put("vnp_Amount", String.valueOf(amount));
//        if (bankCode != null && !bankCode.isEmpty()) {
//            vnpParamsMap.put("vnp_BankCode", bankCode);
//        }
//        vnpParamsMap.put("vnp_IpAddr", VNPayUtil.getIpAddress(request));
//        //build query url
//        String queryUrl = VNPayUtil.getPaymentURL(vnpParamsMap, true);
//        String hashData = VNPayUtil.getPaymentURL(vnpParamsMap, true);
//        String vnpSecureHash = VNPayUtil.hmacSHA512(vnPayConfig.getSecretKey(), hashData);
//        queryUrl += "&vnp_SecureHash=" + vnpSecureHash;
//        String paymentUrl = vnPayConfig.getVnp_PayUrl() + "?" + queryUrl;
//        return PaymentResponse.builder()
//
//                .message("success")
//                .paymentUrl(paymentUrl)
//                .build();
        return null;
    }

    @Override
    public void success(String borrowingId, Long amount, PaymentMethod method) {

    }

    @Override
    public void fail(String borrowingId, Long amount, PaymentMethod method) {

    }




}
