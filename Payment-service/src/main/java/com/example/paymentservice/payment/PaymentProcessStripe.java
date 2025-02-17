package com.example.paymentservice.payment;

import com.example.common.DTO.BookPurchaseDTO;
import com.example.common.enums.ErrorCode;
import com.example.common.enums.Status;
import com.example.common.exception.AppException;
import com.example.common.request.PaymentRequest;
import com.example.common.response.PaymentResponse;
import com.example.common.enums.PaymentMethod;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.param.checkout.SessionCreateParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import com.stripe.model.checkout.Session;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


@Service
public class PaymentProcessStripe implements PaymentProcessService {

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private PaymentProducer paymentProducer;

    @Value("${payment.stripe.secretKey}")
    private String STRIPE_SECRET_KEY;


    @Override
    public boolean support(String paymentType) {
        return PaymentMethod.STRIPE.name().equals(paymentType);
    }

    @Override
    public PaymentResponse createPayment(PaymentRequest request) {
        // Thiết lập API key của Stripe
        Stripe.apiKey = STRIPE_SECRET_KEY;

        // Danh sách các LineItems
        List<SessionCreateParams.LineItem> lineItems = createLineItems(request.getBookPurchaseDTOS());


        // Tạo Session với danh sách LineItems
        SessionCreateParams params =
                SessionCreateParams.builder()
                        .setMode(SessionCreateParams.Mode.PAYMENT)
                        .setSuccessUrl("http://localhost:8084/payments/success?" +
                                "borrowingId=" + request.getBorrowingId() +
                                "&amount=" + request.getAmount() +
                                "&method=" + request.getPaymentMethod()
                        )
                        .setCancelUrl("http://localhost:8084/payments/cancel?" +
                                "borrowingId=" + request.getBorrowingId() +
                                "&method=" + request.getPaymentMethod() +
                                "&amount=" + request.getAmount()
                        )
                        .addAllLineItem(lineItems)
                        .build();
        // Tạo Session
        Session session;
        try {
            session = Session.create(params);
        } catch (StripeException e) {
            throw new AppException(ErrorCode.CREATE_PAYMENT_SESSION_FAILED);
        }

        // Trả về PaymentResponse với URL thanh toán
        return PaymentResponse.builder()
                .message("Payment session created")
                .paymentUrl(session.getUrl())
                .build();
    }

    private List<SessionCreateParams.LineItem> createLineItems(List<BookPurchaseDTO> books) {
        List<SessionCreateParams.LineItem> lineItems = new ArrayList<>();
        for (BookPurchaseDTO book : books) {
            SessionCreateParams.LineItem.PriceData.ProductData productData =
                    SessionCreateParams.LineItem.PriceData.ProductData.builder()
                            .setName(book.getName())
                            .addImage(book.getImage())
                            .build();

            SessionCreateParams.LineItem.PriceData priceData =
                    SessionCreateParams.LineItem.PriceData.builder()
                            .setCurrency("VND")
                            .setUnitAmount(book.getPrice())
                            .setProductData(productData)
                            .build();

            SessionCreateParams.LineItem lineItem =
                    SessionCreateParams.LineItem.builder()
                            .setQuantity(Long.valueOf(book.getQuantity()))
                            .setPriceData(priceData)
                            .build();
            lineItems.add(lineItem);
        }
        return lineItems;
    }


    @Override
    public void success(String borrowingId, Long amount, PaymentMethod method) {
        Payment payment = Payment.builder()
                .paymentMethod(method)
                .amount(amount)
                .borrowingId(borrowingId)
                .status(Status.SUCCESS)
                .build();
        paymentRepository.save(payment);

    }

    @Override
    public void fail(String borrowingId, Long amount, PaymentMethod method) {
         paymentProducer.sendMessage(borrowingId);

        Payment payment = Payment.builder()
                .paymentMethod(method)
                .amount(amount)
                .status(Status.FAIL)
                .borrowingId(borrowingId)
                .build();
        paymentRepository.save(payment);

    }


}