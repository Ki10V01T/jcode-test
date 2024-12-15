package com.github.ki10v01t.jcode_test.service;

import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.github.ki10v01t.jcode_test.entity.Payment;
import com.github.ki10v01t.jcode_test.entity.Wallet;
import com.github.ki10v01t.jcode_test.entity.Dto.PaymentDto;

@Service
public class PaymentValidator implements Validator {
    private final WalletService walletService;

    public PaymentValidator(WalletService walletService) {
        this.walletService = walletService;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return PaymentDto.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        PaymentDto payment = (PaymentDto) target;
        UUID walletId = payment.getWalletId();
        
        if(!walletService.checkExistedWalletById(walletId)) {
            errors.rejectValue("walletId", "" , String.format("Wallet with walletId = %s not exist", walletId));
        }
    }

}
