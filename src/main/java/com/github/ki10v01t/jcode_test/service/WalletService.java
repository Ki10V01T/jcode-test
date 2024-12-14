package com.github.ki10v01t.jcode_test.service;

import java.util.Optional;
import java.util.UUID;
import java.util.regex.Pattern;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.ki10v01t.jcode_test.entity.Wallet;
import com.github.ki10v01t.jcode_test.repository.WalletRepository;

@Service
@Transactional(readOnly = true)
public class WalletService {
    private final WalletRepository walletRepository;

    public UUID transformWalletUUID(String template, Pattern matcher) {
        if(!Wallet.uuidRegex.matcher(template).matches()) {
            throw new IllegalArgumentException("The argument you passed is not valid");
        }

        return UUID.fromString(template);
    }

    public WalletService(WalletRepository walletRepository) {
        this.walletRepository = walletRepository;
    }

    @Transactional
    public void savePayment(Wallet wallet) {
        walletRepository.save(wallet);
    }

    public Optional<Wallet> getWalletById(UUID walletId) {
        return walletRepository.findById(walletId);
    }

}
