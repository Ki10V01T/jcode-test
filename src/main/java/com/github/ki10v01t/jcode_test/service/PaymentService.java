package com.github.ki10v01t.jcode_test.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.ki10v01t.jcode_test.entity.Payment;
import com.github.ki10v01t.jcode_test.entity.Wallet;
import com.github.ki10v01t.jcode_test.entity.Dto.PaymentDto;
import com.github.ki10v01t.jcode_test.exception.NotFoundException;
import com.github.ki10v01t.jcode_test.repository.PaymentRepository;
import com.github.ki10v01t.jcode_test.repository.WalletRepository;

@Service
@Transactional(readOnly = true)
public class PaymentService {
    private final PaymentRepository paymentRepository;
    private final WalletRepository walletRepository;

    public PaymentService(PaymentRepository paymentRepository, WalletRepository walletRepository) {
        this.paymentRepository = paymentRepository;
        this.walletRepository = walletRepository;
    }

    @Transactional
    public void createNewPayment(PaymentDto paymentDto) {
        Optional<Wallet> wallet = walletRepository.findById(paymentDto.getWalletId());
        if(wallet.isEmpty()) {
            throw new NotFoundException("The wallet for the wallet_id you specified was not found");
        }

        paymentRepository.save(paymentDto.toPayment(wallet.get()));
    }

    public List<Payment> getPaymentsByWalletId(UUID walletId) {
        List<Payment> payments = paymentRepository.findAllByWalletId(walletId);
        if(payments.isEmpty()) {
            throw new NotFoundException("The wallet for the wallet_id you specified was not found");
        }

        return payments;
    }
}
