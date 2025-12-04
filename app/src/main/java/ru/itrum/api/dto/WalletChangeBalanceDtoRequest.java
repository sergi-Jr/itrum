package ru.itrum.api.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import ru.itrum.api.entity.OperationType;

import java.math.BigDecimal;
import java.util.UUID;

public record WalletChangeBalanceDtoRequest(@NotNull UUID walletId,
                                            OperationType operationType,
                                            @DecimalMin(value = "0.00", message = "Amount must be positive. At least 0.01 value")
                                            BigDecimal amount) {
}
