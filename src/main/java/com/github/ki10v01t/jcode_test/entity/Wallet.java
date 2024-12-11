package com.github.ki10v01t.jcode_test.entity;

import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotEmpty;
import java.util.Objects;

@Entity
@Table(name = "wallet")
public class Wallet {
    @Id
    @Column(name = "wallet_id")
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID walletId;

    @Column(name = "full_name")
    @NotEmpty(message = "Поле fullName не должно быть пустым")
    private String fullName;

    public Wallet() {
    }

    public Wallet(String fullName) {
        this.fullName = fullName;
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
    

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof Wallet)) {
            return false;
        }
        Wallet wallet = (Wallet) o;
        return Objects.equals(walletId, wallet.walletId) && Objects.equals(fullName, wallet.fullName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(walletId, fullName);
    }

    @Override
    public String toString() {
        return "{" +
            " walletId='" + getWalletId() + "'" +
            ", fullName='" + getFullName() + "'" +
            "}";
    }

    
}
