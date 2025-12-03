package ru.itrum.api.facade;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
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

@Component
@RequiredArgsConstructor
public class WalletFacade {
    private final WalletService walletService;
    private final WalletMapper walletMapper;

    @Transactional
    public WalletDtoResponse changeBalance(WalletChangeBalanceDtoRequest walletRequest) {
        Wallet wallet = walletService.getByIdLockForUpdate(walletRequest.walletId());

        OperationType operationType = walletRequest.operationType();
        BigDecimal newWalletAmount = switch (operationType) {
            case DEPOSIT -> wallet.getAmount().add(walletRequest.amount());
            case WITHDRAW -> {
                if (wallet.getAmount().compareTo(walletRequest.amount()) < 0) {
                    NotEnoughMoneyException innerException = new NotEnoughMoneyException("Not enough money on wallet: " + wallet.getId());
                    throw new WalletException(innerException);
                } else {
                    yield wallet.getAmount().subtract(walletRequest.amount());
                }
            }
        };

        wallet.setAmount(newWalletAmount);
        Wallet updatedWallet = walletService.save(wallet);

        return walletMapper.toDto(updatedWallet);
    }

    public WalletDtoResponse getById(UUID walletId) {
        Wallet wallet = walletService.getById(walletId);
        return walletMapper.toDto(wallet);
    }
}
