package com.example.paymentservice.payment;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
        Payment getPaymentByBorrowingId(String id);
        List<Payment>  findAllByBorrowingIdIn(List<String> borrowingId);
}
