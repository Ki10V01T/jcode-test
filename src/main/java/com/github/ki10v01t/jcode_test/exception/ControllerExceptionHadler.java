package com.github.ki10v01t.jcode_test.exception;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.github.ki10v01t.jcode_test.entity.Dto.PaymentErrorResponseDto;

@ControllerAdvice
public class ControllerExceptionHadler extends ResponseEntityExceptionHandler {

    @ExceptionHandler
    public ResponseEntity<PaymentErrorResponseDto> handleNotFoundException(NotFoundException pnfe) {
        return new ResponseEntity<>(new PaymentErrorResponseDto(pnfe.getMessage(), LocalDateTime.now()), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler
    public ResponseEntity<PaymentErrorResponseDto> handleIllegalArgumentException(IllegalArgumentException iae) {
        return new ResponseEntity<>(new PaymentErrorResponseDto(iae.getMessage(), LocalDateTime.now()), HttpStatus.BAD_REQUEST);
    }
}
