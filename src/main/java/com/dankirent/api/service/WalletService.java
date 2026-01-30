package com.dankirent.api.service;

import com.dankirent.api.model.wallet.Wallet;
import com.dankirent.api.repository.WalletRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class WalletService {

    private final WalletRepository repository;

    public Wallet create(Wallet wallet) {
        return repository.save(wallet);
    }
}
