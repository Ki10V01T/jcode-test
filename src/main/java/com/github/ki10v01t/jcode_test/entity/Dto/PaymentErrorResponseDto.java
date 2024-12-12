package com.github.ki10v01t.jcode_test.entity.Dto;

import java.time.LocalDateTime;

public class PaymentErrorResponseDto {
    private String message;
    private LocalDateTime timestamp;

    public PaymentErrorResponseDto(String message, LocalDateTime timestamp) {
        this.message = message;
        this.timestamp = timestamp;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public String getMessage() {
        return message;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

}
