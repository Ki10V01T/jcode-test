package com.github.ki10v01t.jcode_test.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.github.ki10v01t.jcode_test.entity.Payment;
import com.github.ki10v01t.jcode_test.entity.Wallet;
import com.github.ki10v01t.jcode_test.entity.Dto.PaymentDto;
import com.github.ki10v01t.jcode_test.entity.Dto.WalletDto;
import com.github.ki10v01t.jcode_test.exception.NotFoundException;
import com.github.ki10v01t.jcode_test.service.PaymentService;
import com.github.ki10v01t.jcode_test.service.PaymentValidator;
import com.github.ki10v01t.jcode_test.service.WalletService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1")
public class WalletApiRestController {

    private final PaymentService paymentService;
    private final WalletService walletService;
    private final PaymentValidator paymentValidator;

    public WalletApiRestController(PaymentService paymentService, WalletService walletService, PaymentValidator paymentValidator) {
        this.paymentService = paymentService;
        this.walletService = walletService;
        this.paymentValidator = paymentValidator;
    }

    @GetMapping("/wallets/payments/{wallet-id}")
    public ResponseEntity<List<PaymentDto>> getPaymentsByWalletId(@PathVariable("wallet-id") String walletIdTemplate) {
        
        UUID walletId = walletService.transformWalletUUID(walletIdTemplate, Wallet.uuidRegex);

        List<Payment> payments = paymentService.getPaymentsByWalletId(walletId);

        List<PaymentDto> paymentsResult = new ArrayList<>(payments.size());
        for(Payment p : payments) {
            paymentsResult.add(new PaymentDto.PaymentDtoBuilder().setWalletId(walletId).setOperationType(p.getOperationType()).setAmount(p.getAmount()).build());
        }

        return ResponseEntity.ok().body(paymentsResult);
    }

    @GetMapping("/wallets/{wallet-id}")
    public ResponseEntity<WalletDto> getWalletBalanceByWalletId(@PathVariable("wallet-id") String walletIdTemplate) {
        
        UUID walletId = walletService.transformWalletUUID(walletIdTemplate, Wallet.uuidRegex);

        Wallet wallet = walletService.getWalletById(walletId);
        

        return ResponseEntity.ok().body(new WalletDto.WalletDtoBuilder().setBalance(wallet.getBalance()).build());
    }

    

    @PostMapping("/wallet")
    public ResponseEntity<HttpStatus> saveNewPayment(@RequestBody @Valid PaymentDto paymentDto, BindingResult bindingResult) {
        paymentValidator.validate(paymentDto, bindingResult);
        //Optional<Wallet> w = walletService.getWalletById(null);
        if(bindingResult.hasErrors()) {
            StringBuilder errorMessage = new StringBuilder();

            List<FieldError> errors = bindingResult.getFieldErrors(); 
            
            for(FieldError error : errors) {
                errorMessage.append(error.getField())
                            .append("-")
                            .append(error.getDefaultMessage())
                            .append(";");
            }
            throw new NotFoundException(errorMessage.toString());
        }

        walletService.updateBalance(paymentDto);
        paymentService.createNewPayment(paymentDto);
        return ResponseEntity.ok().build();
    }
}
