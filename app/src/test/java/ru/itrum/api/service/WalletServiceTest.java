package ru.itrum.api.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.itrum.api.entity.Wallet;
import ru.itrum.api.repository.WalletRepository;

import java.math.BigDecimal;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class WalletServiceTest {

    @Mock
    private WalletRepository walletRepository;

    @InjectMocks
    private WalletService walletService;

    private Wallet testWallet;
    private static final UUID WALLET_ID = UUID.randomUUID();

    @BeforeEach
    void setUp() {
        testWallet = new Wallet();
        testWallet.setId(WALLET_ID);
        testWallet.setAmount(new BigDecimal("1000.00"));
    }

    @Test
    void save_Success() {
        when(walletRepository.save(any(Wallet.class))).thenReturn(testWallet);

        Wallet result = walletService.save(testWallet);

        assertEquals(testWallet, result);
        verify(walletRepository).save(testWallet);
    }

    @Test
    void save_RepositoryThrowsException_Propagates() {
        when(walletRepository.save(testWallet))
                .thenThrow(new RuntimeException("DB error"));

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> walletService.save(testWallet));

        assertEquals("DB error", exception.getMessage());
        verify(walletRepository).save(testWallet);
    }

    @Test
    void getByIdLockForUpdate_ExistingWallet_ReturnsWallet() {
        when(walletRepository.findByIdLockForUpdate(WALLET_ID))
                .thenReturn(Optional.of(testWallet));

        Wallet result = walletService.getByIdLockForUpdate(WALLET_ID);

        assertEquals(testWallet, result);
        verify(walletRepository).findByIdLockForUpdate(WALLET_ID);
    }

    @Test
    void getByIdLockForUpdate_NonExistingWallet_ThrowsNoSuchElementException() {
        UUID nonExistingId = UUID.randomUUID();
        when(walletRepository.findByIdLockForUpdate(nonExistingId))
                .thenReturn(Optional.empty());

        NoSuchElementException exception = assertThrows(NoSuchElementException.class,
                () -> walletService.getByIdLockForUpdate(nonExistingId));

        assertTrue(exception.getMessage().contains("Wallet not found, id: " + nonExistingId));
        verify(walletRepository).findByIdLockForUpdate(nonExistingId);
    }

    @Test
    void getByIdLockForUpdate_RepositoryThrowsException_Propagates() {
        when(walletRepository.findByIdLockForUpdate(WALLET_ID))
                .thenThrow(new RuntimeException("DB connection error"));

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> walletService.getByIdLockForUpdate(WALLET_ID));

        assertEquals("DB connection error", exception.getMessage());
        verify(walletRepository).findByIdLockForUpdate(WALLET_ID);
    }

    @Test
    void getById_Success() {
        when(walletRepository.findById(WALLET_ID)).thenReturn(Optional.of(testWallet));

        Wallet result = walletService.getById(WALLET_ID);
        assertEquals(testWallet, result);
        verify(walletRepository).findById(WALLET_ID);
    }

    @Test
    void getById_NonExistingWallet_ThrowsNoSuchElementException() {
        UUID nonExistingId = UUID.randomUUID();
        when(walletRepository.findById(nonExistingId))
                .thenReturn(Optional.empty());

        NoSuchElementException exception = assertThrows(NoSuchElementException.class,
                () -> walletService.getById(nonExistingId));

        assertTrue(exception.getMessage().contains("Wallet not found, id: " + nonExistingId));
        verify(walletRepository).findById(nonExistingId);
    }
}
