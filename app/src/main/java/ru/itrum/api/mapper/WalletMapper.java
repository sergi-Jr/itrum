package ru.itrum.api.mapper;

import org.springframework.stereotype.Component;
import ru.itrum.api.dto.WalletDtoResponse;
import ru.itrum.api.entity.Wallet;

@Component
public class WalletMapper {

    public WalletDtoResponse toDto(Wallet wallet) {
        return new WalletDtoResponse(wallet.getId(), wallet.getAmount());
    }
}
