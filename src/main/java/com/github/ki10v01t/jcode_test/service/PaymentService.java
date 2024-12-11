package com.github.ki10v01t.jcode_test.service;

import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.ki10v01t.jcode_test.entity.Payment;
import com.github.ki10v01t.jcode_test.repository.PaymentRepository;

@Service
@Transactional(readOnly = true)
public class PaymentService {
    private final PaymentRepository paymentRepository;

    public PaymentService(PaymentRepository paymentRepository) {
        this.paymentRepository = paymentRepository;
    }


    public void createNewPayment(Payment payment) {
        paymentRepository.save(payment);
    }

    public Optional<Payment> getOnePaymentByWalletId(UUID id) {
        return paymentRepository.findByWalletId(id);
    }
}
