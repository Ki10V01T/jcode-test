package com.github.ki10v01t.jcode_test.entity.Dto;

import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.github.ki10v01t.jcode_test.entity.Wallet;

public class WalletDto {
    @JsonIgnore
    private UUID walletId;
    @JsonIgnore
    private String fullName;
    private Long balance;

    private WalletDto(WalletDtoBuilder walletDtoBuilder) {
        this.walletId = walletDtoBuilder.walletId;
        this.fullName = walletDtoBuilder.fullName;
        this.balance = walletDtoBuilder.balance;
    }

    @JsonIgnoreProperties
    public static class WalletDtoBuilder {
        private UUID walletId;
        private String fullName;
        private Long balance;
        
        public WalletDtoBuilder() {}

        public WalletDtoBuilder setWalletId(UUID walletId) {
            this.walletId = walletId;
            return this;
        }

        public WalletDtoBuilder setFullName(String fullName) {
            this.fullName = fullName;
            return this;
        }

        public WalletDtoBuilder setBalance(Long balance) {
            this.balance = balance;
            return this;
        }

        public WalletDto build() {
            return new WalletDto(this);
        }
    }

    public void setWalletId(UUID walletId) {
        this.walletId = walletId;
    }

    public void setBalance(Long balance) {
        this.balance = balance;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public UUID getWalletId() {
        return walletId;
    }

    public Long getBalance() {
        return balance;
    }

    public String getFullName() {
        return fullName;
    }

    public Wallet toWallet() {
        Wallet wallet = new Wallet();
        wallet.setWalletId(this.walletId);
        wallet.setFullName(this.fullName);
        wallet.setBalance(this.balance);

        return wallet;
    }

}
