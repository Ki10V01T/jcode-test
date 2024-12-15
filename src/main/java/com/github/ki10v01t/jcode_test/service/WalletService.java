package com.github.ki10v01t.jcode_test.service;

import java.util.Optional;
import java.util.UUID;
import java.util.regex.Pattern;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.ki10v01t.jcode_test.entity.Wallet;
import com.github.ki10v01t.jcode_test.entity.Dto.PaymentDto;
import com.github.ki10v01t.jcode_test.exception.NotFoundException;
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

    @Transactional
    public void updateBalance(PaymentDto paymentDto) {
        Wallet wallet = walletRepository.findById(paymentDto.getWalletId()).get();
        switch (paymentDto.getOperationType()) {
            case DEPOSIT -> {
                if(paymentDto.getAmount() > Long.MAX_VALUE) {
                    throw new IllegalArgumentException("Передано слишком большое значение");
                }
                if(wallet.getBalance() + paymentDto.getAmount() > Long.MAX_VALUE) {
                    throw new IllegalArgumentException("Кошелёк переполнен");
                }
                Long result = wallet.getBalance() + paymentDto.getAmount();
                wallet.setBalance(result);
            }
            case WITHDRAW -> {
                if(wallet.getBalance() - paymentDto.getAmount() < 0) {
                    throw new IllegalArgumentException("Недостаточно средств для списания");
                }
                Long result = wallet.getBalance() - paymentDto.getAmount();
                wallet.setBalance(result);
            }
        
            default -> {
                throw new IllegalArgumentException("Неизвестная ошибка");
            }
        }
    }

    public Boolean checkExistedWalletById(UUID walletId) {
        return walletRepository.existsById(walletId);
    }

    public Wallet getWalletById(UUID walletId) throws NotFoundException{
        Optional<Wallet> wallet = walletRepository.findById(walletId);
        if(wallet.isEmpty()) {
            throw new NotFoundException("The wallet for the wallet_id you specified was not found");
        }

        return wallet.get();
    }

}
