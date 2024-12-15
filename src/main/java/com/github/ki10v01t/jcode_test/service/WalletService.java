package com.github.ki10v01t.jcode_test.service;

import java.util.Optional;
import java.util.UUID;
import java.util.regex.Pattern;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.ki10v01t.jcode_test.entity.Wallet;
import com.github.ki10v01t.jcode_test.entity.Dto.WalletDto;
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

    public Boolean checkExistedWalletById(UUID walletId) {
        return walletRepository.existsById(walletId);
    } 

    public WalletDto getWalletById(UUID walletId) throws NotFoundException{
        Optional<Wallet> wallet = walletRepository.findById(walletId);
        if(wallet.isEmpty()) {
            throw new NotFoundException("The wallet for the wallet_id you specified was not found");
        }

        return new WalletDto.WalletDtoBuilder().setBalance(wallet.get().getBalance()).build();
    }

}
