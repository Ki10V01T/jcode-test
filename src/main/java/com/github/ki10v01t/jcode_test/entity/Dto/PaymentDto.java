package com.github.ki10v01t.jcode_test.entity.Dto;

import java.util.UUID;

import com.github.ki10v01t.jcode_test.entity.OperationType;

public class PaymentDto {
    private UUID walletId;
    private OperationType operationType;
    private Long amount;

    public PaymentDto() {}

    public PaymentDto(UUID walletId, OperationType operationType, Long amount) {
        this.walletId = walletId;
        this.operationType = operationType;
        this.amount = amount;
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
