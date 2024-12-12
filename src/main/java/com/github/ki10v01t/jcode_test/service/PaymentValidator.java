package com.github.ki10v01t.jcode_test.service;

import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.github.ki10v01t.jcode_test.entity.Payment;
import com.github.ki10v01t.jcode_test.entity.Wallet;

@Service
public class PaymentValidator implements Validator {
    private final WalletService walletService;

    public PaymentValidator(WalletService walletService) {
        this.walletService = walletService;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return Wallet.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        Payment payment = (Payment) target;
        UUID walletId = payment.getWallet().getWalletId();
        
        if(!walletService.getWalletById(walletId).isPresent()) {
            errors.rejectValue("wallet", "" , String.format("Wallet with walletID = %s not exist", walletId));
        }
    }

}
