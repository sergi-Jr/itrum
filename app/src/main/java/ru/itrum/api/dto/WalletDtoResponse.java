package ru.itrum.api.dto;

import java.math.BigDecimal;
import java.util.UUID;

public record WalletDtoResponse(UUID walletId, BigDecimal balance) {
}
