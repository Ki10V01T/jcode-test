package com.github.ki10v01t.jcode_test.entity.Dto;

import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonIgnoreType;
import com.github.ki10v01t.jcode_test.entity.OperationType;

public class PaymentDto {
    private UUID walletId;
    private OperationType operationType;
    private Long amount;

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
}
