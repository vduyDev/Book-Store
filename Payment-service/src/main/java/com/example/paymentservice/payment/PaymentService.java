package com.example.paymentservice.payment;


import com.example.common.DTO.PaymentDTO;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class PaymentService {

    private  final PaymentRepository paymentRepository;


    public PaymentDTO getPaymentByBorrowingId(String id) {
        Payment payment = paymentRepository.getPaymentByBorrowingId(id);
        return PaymentMapper.toPaymentDTO(payment);
    }

    public List<PaymentDTO> getListPayment() {
        List<Payment> payments = paymentRepository.findAll();
        return payments.stream().map(PaymentMapper::toPaymentDTO).toList();
    }

    public List<PaymentDTO> getPaymentByListBorrowing(List<String> ids) {
        List<Payment> payments = paymentRepository.findAllByBorrowingIdIn(ids);
        return payments.stream().map(PaymentMapper::toPaymentDTO).toList();
    }
}
