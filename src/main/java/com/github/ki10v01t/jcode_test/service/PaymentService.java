package com.github.ki10v01t.jcode_test.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.ki10v01t.jcode_test.entity.Payment;
import com.github.ki10v01t.jcode_test.entity.Dto.PaymentDto;
import com.github.ki10v01t.jcode_test.exception.PaymentNotFoundException;
import com.github.ki10v01t.jcode_test.repository.PaymentRepository;

@Service
@Transactional(readOnly = true)
public class PaymentService {
    private final PaymentRepository paymentRepository;

    public PaymentService(PaymentRepository paymentRepository) {
        this.paymentRepository = paymentRepository;
    }

    @Transactional
    public void createNewPayment(Payment payment) {
        paymentRepository.save(payment);
    }

    public List<PaymentDto> getPaymentsByWalletId(UUID walletId) {
        List<Payment> payments = paymentRepository.findAllByWalletId(walletId);
        if(payments.size() == 0) {
            throw new PaymentNotFoundException("The wallet for the wallet_id you specified was not found");
        }

        List<PaymentDto> paymentsResult = new ArrayList<>(payments.size());
        for(Payment p : payments) {
            paymentsResult.add(new PaymentDto(p.getWallet().getWalletId(), p.getOperationType(), p.getAmount()));
        }

        return paymentsResult;
    }
}
