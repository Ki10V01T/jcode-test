package com.github.ki10v01t.jcode_test.entity;

import java.util.Objects;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "payment")
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID paymentId;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "wallet_id", referencedColumnName = "wallet_id")
    @JsonBackReference
    private Wallet wallet;

    @Column(name = "operation_type")
    @Enumerated(EnumType.STRING)
    @NotNull(message = "Поле operationType не должно быть пустым")
    private OperationType operationType;

    @Column(name = "amount")
    @NotNull(message = "Поле amount не должно быть пустым")
    @Min(value = 0L, message = "Значение поля amount, не может быть меньше нуля")
    @Max(value = 9223372036854775806L, message = "Значение поля amount не может быть слишком большим")
    private Long amount;


    public Payment() {
    }

    public Payment(Wallet wallet, OperationType operationType, Long amount) {
        this.wallet = wallet;
        this.operationType = operationType;
        this.amount = amount;
    }
    
    public void setPaymentId(UUID paymentId) {
        this.paymentId = paymentId;
    }

    public UUID getPaymentId() {
        return paymentId;
    }

    public Wallet getWallet() {
        return this.wallet;
    }

    public void setWallet(Wallet wallet) {
        this.wallet = wallet;
    }

    public OperationType getOperationType() {
        return this.operationType;
    }

    public void setOperationType(OperationType operationType) {
        this.operationType = operationType;
    }

    public Long getAmount() {
        return this.amount;
    }

    public void setAmount(Long amount) {
        this.amount = amount;
    }


    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof Payment)) {
            return false;
        }
        Payment payment = (Payment) o;
        return Objects.equals(paymentId, payment.paymentId) && Objects.equals(wallet, payment.wallet) && Objects.equals(operationType, payment.operationType) && Objects.equals(amount, payment.amount);
    }

    @Override
    public int hashCode() {
        return Objects.hash(paymentId, wallet, operationType, amount);
    }

    @Override
    public String toString() {
        return "{" +
            " paymentId='" + getPaymentId() + "'" +
            ", wallet='" + getWallet() + "'" +
            ", operationType='" + getOperationType() + "'" +
            ", amount='" + getAmount() + "'" +
            "}";
    }



}
