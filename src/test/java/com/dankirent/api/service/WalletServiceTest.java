package com.dankirent.api.service;

import com.dankirent.api.model.wallet.Wallet;
import com.dankirent.api.repository.WalletRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class WalletServiceTest {

    @Mock
    private WalletRepository walletRepository;

    @InjectMocks
    private WalletService service;

    Wallet wallet;

    @BeforeEach
    void setUp() {
        wallet = new Wallet(null, null, 0.0);
    }

    @Test
    void shouldCreateWalletSuccessfully() {
        when(walletRepository.save(any(Wallet.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Wallet result = service.create(wallet);

        assertNotNull(result);
        verify(walletRepository, times(1)).save(wallet);

    }
}
