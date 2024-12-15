package com.github.ki10v01t.jcode_test.entity.Dto;

import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.github.ki10v01t.jcode_test.entity.OperationType;
import com.github.ki10v01t.jcode_test.entity.Payment;
import com.github.ki10v01t.jcode_test.entity.Wallet;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public class PaymentDto {
    @NotNull(message = "Поле walletId не должно быть пустым")
    private UUID walletId;

    @NotNull(message = "Поле operationType не должно быть пустым")
    private OperationType operationType;

    @Min(value = 0L, message = "Значение поля amount, не может быть меньше нуля")
    @Max(value = 9223372036854775806L, message = "Значение поля amount не может быть слишком большим")
    @NotNull(message = "Поле amount не должно быть пустым")
    private Long amount;


    public PaymentDto(){}

    private PaymentDto(PaymentDtoBuilder paymentDtoBuilder) {
        this.walletId = paymentDtoBuilder.walletId;
        this.operationType = paymentDtoBuilder.operationType;
        this.amount = paymentDtoBuilder.amount;
    }

    @JsonIgnoreProperties
    public static class PaymentDtoBuilder {
        private UUID walletId;
        private OperationType operationType;
        private Long amount;

        public PaymentDtoBuilder() {}

        public PaymentDtoBuilder setAmount(Long amount) {
            this.amount = amount;
            return this;
        }

        public PaymentDtoBuilder setOperationType(OperationType operationType) {
            this.operationType = operationType;
            return this;
        }

        public PaymentDtoBuilder setWalletId(UUID walletId) {
            this.walletId = walletId;
            return this;
        }

        public PaymentDto build() {
            return new PaymentDto(this);
        }
    }

    public void setAmount(Long amount) {
        this.amount = amount;
    }

    public void setOperationType(OperationType operationType) {
        this.operationType = operationType;
    }

    public void setWalletId(UUID walletId) {
        this.walletId = walletId;
    }

    public Long getAmount() {
        return amount;
    }

    public OperationType getOperationType() {
        return operationType;
    }
    
    public UUID getWalletId() {
        return walletId;
    }

    public Payment toPayment(Wallet wallet) {
        Payment payment = new Payment(wallet, this.operationType, this.amount);

        return payment;
    }
}
