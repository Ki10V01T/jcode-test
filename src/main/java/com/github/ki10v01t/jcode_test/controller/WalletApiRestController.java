package com.github.ki10v01t.jcode_test.controller;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.regex.Pattern;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.github.ki10v01t.jcode_test.entity.Payment;
import com.github.ki10v01t.jcode_test.entity.Wallet;
import com.github.ki10v01t.jcode_test.entity.Dto.PaymentDto;
import com.github.ki10v01t.jcode_test.entity.Dto.PaymentErrorResponseDto;
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

        // Optional<Wallet> wallet = walletService.getWalletById(walletId);
        // if(wallet.isEmpty()) {
        //     throw new PaymentNotFoundException("The wallet for the wallet_id you specified was not found");
        // }

        List<PaymentDto> payments = paymentService.getPaymentsByWalletId(walletId);
        //return ResponseEntity.ok().body(wallet.get().getPayments());
        return ResponseEntity.ok().body(payments);
    }

    @GetMapping("/wallets/{wallet-id}")
    public ResponseEntity<WalletDto> getWalletBalanceByWalletId(@PathVariable("wallet-id") String walletIdTemplate) {
        
        UUID walletId = walletService.transformWalletUUID(walletIdTemplate, Wallet.uuidRegex);

        WalletDto wallet = walletService.getWalletById(walletId);
        

        return ResponseEntity.ok().body(wallet);
        //List<PaymentDto> payments = paymentService.getPaymentsByWalletId(walletId);
        //return ResponseEntity.ok().body(wallet.get().getPayments());
        //return ResponseEntity.ok().body(payments);
    }

    

    @PostMapping("/wallet")
    public ResponseEntity<HttpStatus> saveNewPayment(@RequestBody @Valid Payment payment, BindingResult bindingResult) {
        paymentValidator.validate(payment, bindingResult);
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
        paymentService.createNewPayment(payment);
        return ResponseEntity.ok().build();
    }

    @ExceptionHandler
    public ResponseEntity<PaymentErrorResponseDto> handleNotFoundException(NotFoundException pnfe) {
        return new ResponseEntity<>(new PaymentErrorResponseDto(pnfe.getMessage(), LocalDateTime.now()), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler
    public ResponseEntity<PaymentErrorResponseDto> handleIllegalArgumentException(IllegalArgumentException iae) {
        return new ResponseEntity<>(new PaymentErrorResponseDto(iae.getMessage(), LocalDateTime.now()), HttpStatus.BAD_REQUEST);
    }
}
