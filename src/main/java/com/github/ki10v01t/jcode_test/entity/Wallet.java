package com.github.ki10v01t.jcode_test.entity;

import java.util.UUID;
import java.util.regex.Pattern;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "wallet")
public class Wallet {
    @Transient
    public static final Pattern uuidRegex = Pattern.compile("^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}$");

    @Id
    @Column(name = "wallet_id")
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID walletId;

    @Column(name = "full_name")
    @NotEmpty(message = "Поле fullName не должно быть пустым")
    private String fullName;

    @Column(name = "balance")
    @NotNull(message = "Поле balance не должно быть пустым")
    @Min(value = 0L, message = "Значение поля amount, не может быть меньше нуля")
    @Max(value = 9223372036854775806L, message = "Значение поля amount не может быть слишком большим")
    private Long balance;

    @OneToMany(mappedBy = "wallet", cascade = {CascadeType.PERSIST})
    private List<Payment> payments;

    public Wallet() {
    }

    public Wallet(String walletIdTemplate) throws IllegalArgumentException {
        this.walletId = UUID.fromString(walletIdTemplate);
    }
    

    public UUID getWalletId() {
        return this.walletId;
    }

    public void setWalletId(UUID walletId) {
        this.walletId = walletId;
    }


    public String getFullName() {
        return this.fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public void setPayments(List<Payment> payments) {
        this.payments = payments;
    }
    
    public List<Payment> getPayments() {
        return payments;
    }

    public void setBalance(Long balance) {
        this.balance = balance;
    }

    public Long getBalance() {
        return balance;
    }


    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof Wallet)) {
            return false;
        }
        Wallet wallet = (Wallet) o;
        return Objects.equals(walletId, wallet.walletId) && Objects.equals(fullName, wallet.fullName) && Objects.equals(balance, wallet.balance) && Objects.equals(payments, wallet.payments);
    }

    @Override
    public int hashCode() {
        return Objects.hash(walletId, fullName, balance, payments);
    }

    @Override
    public String toString() {
        return "{" +
            " walletId='" + getWalletId() + "'" +
            ", fullName='" + getFullName() + "'" +
            ", balance='" + getBalance() + "'" +
            ", payments='" + getPayments() + "'" +
            "}";
    }
    

    
}
