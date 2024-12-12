package com.github.ki10v01t.jcode_test.exception;

public class PaymentNotFoundException extends RuntimeException {
    public PaymentNotFoundException(String msg) {
        super(msg);
    }
}
