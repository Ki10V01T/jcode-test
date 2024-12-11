package com.github.ki10v01t.jcode_test.controller;

import java.util.Optional;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.github.ki10v01t.jcode_test.entity.Payment;
import com.github.ki10v01t.jcode_test.entity.Wallet;
import com.github.ki10v01t.jcode_test.service.PaymentService;
import com.github.ki10v01t.jcode_test.service.WalletService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1")
public class WalletApiRestController {

    private final PaymentService paymentService;
    private final WalletService walletService;

    public WalletApiRestController(PaymentService paymentService, WalletService walletService) {
        this.paymentService = paymentService;
        this.walletService = walletService;
    }

    @GetMapping("/wallets/{wallet-id}")
    public ResponseEntity<Payment> getOnePaymentByWalletId(@PathVariable("wallet_id") Optional<UUID> walletId) {
        if(walletId.isEmpty()) {
            return null;
        } 
        Optional<Payment> payment = paymentService.getOnePaymentByWalletId(walletId.get());
        if(payment.isEmpty()) {
            return null;
        }
        
        return ResponseEntity.ok().body(payment.get());


    }

    @PostMapping("/wallet")
    public ResponseEntity<HttpStatus> saveNewPayment(@RequestBody @Valid Payment payment, BindingResult bindingResult) {
        if(bindingResult.hasErrors()) {
            return null;
        }
        paymentService.createNewPayment(payment);
        return ResponseEntity.ok().build();
    }

}
