package ru.itrum.api.facade;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.itrum.api.dto.WalletChangeBalanceDtoRequest;
import ru.itrum.api.dto.WalletDtoResponse;
import ru.itrum.api.entity.OperationType;
import ru.itrum.api.entity.Wallet;
import ru.itrum.api.exception.NotEnoughMoneyException;
import ru.itrum.api.exception.WalletException;
import ru.itrum.api.mapper.WalletMapper;
import ru.itrum.api.service.WalletService;

import java.math.BigDecimal;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.argThat;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class WalletFacadeTest {

    @Mock
    private WalletService walletService;

    @Mock
    private WalletMapper walletMapper;

    @InjectMocks
    private WalletFacade walletFacade;

    private Wallet testWallet;
    private WalletChangeBalanceDtoRequest request;
    private WalletDtoResponse expectedResponse;
    private static final UUID WALLET_ID = UUID.randomUUID();

    @BeforeEach
    void setUp() {
        testWallet = new Wallet();
        testWallet.setId(WALLET_ID);
        testWallet.setAmount(new BigDecimal("1000.00"));

        request = new WalletChangeBalanceDtoRequest(
                WALLET_ID,
                OperationType.DEPOSIT,
                new BigDecimal("100.00")
        );

        expectedResponse = new WalletDtoResponse(WALLET_ID);
    }

    @Test
    void changeBalance_Deposit_Success() {
        when(walletService.getByIdLockForUpdate(WALLET_ID)).thenReturn(testWallet);
        Wallet updatedWallet = new Wallet();
        updatedWallet.setId(WALLET_ID);
        when(walletService.save(any(Wallet.class))).thenReturn(updatedWallet);
        when(walletMapper.toDto(updatedWallet)).thenReturn(expectedResponse);

        WalletDtoResponse result = walletFacade.changeBalance(request);

        assertEquals(expectedResponse, result);
        verify(walletService).getByIdLockForUpdate(WALLET_ID);
        verify(walletService).save(argThat(wallet ->
                wallet.getAmount().equals(new BigDecimal("1100.00"))));
        verify(walletMapper).toDto(updatedWallet);
    }

    @Test
    void changeBalance_Withdraw_Success() {
        request = new WalletChangeBalanceDtoRequest(WALLET_ID, OperationType.WITHDRAW, new BigDecimal("200.00"));
        when(walletService.getByIdLockForUpdate(WALLET_ID)).thenReturn(testWallet);
        Wallet updatedWallet = new Wallet();
        updatedWallet.setId(WALLET_ID);
        when(walletService.save(any(Wallet.class))).thenReturn(updatedWallet);
        when(walletMapper.toDto(updatedWallet)).thenReturn(expectedResponse);

        WalletDtoResponse result = walletFacade.changeBalance(request);

        assertEquals(expectedResponse, result);
        verify(walletService).save(argThat(wallet ->
                wallet.getAmount().equals(new BigDecimal("800.00"))));
    }

    @Test
    void changeBalance_Withdraw_NotEnoughMoney_ThrowsException() {
        request = new WalletChangeBalanceDtoRequest(WALLET_ID, OperationType.WITHDRAW, new BigDecimal("1500.00"));
        when(walletService.getByIdLockForUpdate(WALLET_ID)).thenReturn(testWallet);

        WalletException exception = assertThrows(WalletException.class,
                () -> walletFacade.changeBalance(request));

        assertInstanceOf(NotEnoughMoneyException.class, exception.getCause());
        verify(walletService, never()).save(any());
        verify(walletMapper, never()).toDto(any());
    }

    @Test
    void changeBalance_ServiceGetById_ThrowsException() {
        when(walletService.getByIdLockForUpdate(WALLET_ID))
                .thenThrow(new RuntimeException("Wallet not found"));

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> walletFacade.changeBalance(request));

        assertEquals("Wallet not found", exception.getMessage());
        verify(walletService, never()).save(any());
    }
}