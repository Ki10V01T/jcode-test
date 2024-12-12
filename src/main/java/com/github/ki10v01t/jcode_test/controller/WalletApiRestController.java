package com.github.ki10v01t.jcode_test.controller;

import java.time.LocalDateTime;
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
import com.github.ki10v01t.jcode_test.entity.Dto.PaymentErrorResponseDto;
import com.github.ki10v01t.jcode_test.exception.PaymentNotFoundException;
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

    @GetMapping("/wallets/{wallet-id}")
    public ResponseEntity<Payment> getOnePaymentByWalletId(@PathVariable("wallet-id") String walletIdTemplate) {
        if(!Wallet.uuidRegex.matcher(walletIdTemplate).matches()) {
            throw new IllegalArgumentException("The argument you passed is not valid");
        }
        UUID walletId = UUID.fromString(walletIdTemplate);

        Optional<Payment> payment = paymentService.getOnePaymentByWalletId(walletId);
        if(payment.isEmpty()) {
            throw new PaymentNotFoundException("The wallet for the wallet_id you specified was not found");
        }
        
        return ResponseEntity.ok().body(payment.get());
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
            throw new PaymentNotFoundException(errorMessage.toString());
        }
        paymentService.createNewPayment(payment);
        return ResponseEntity.ok().build();
    }

    @ExceptionHandler
    public ResponseEntity<PaymentErrorResponseDto> handlePaymentNotFound(PaymentNotFoundException pnfe) {
        return new ResponseEntity<>(new PaymentErrorResponseDto(pnfe.getMessage(), LocalDateTime.now()), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler
    public ResponseEntity<PaymentErrorResponseDto> handleIllegalArgumentException(IllegalArgumentException iae) {
        return new ResponseEntity<>(new PaymentErrorResponseDto(iae.getMessage(), LocalDateTime.now()), HttpStatus.BAD_REQUEST);
    }
}
