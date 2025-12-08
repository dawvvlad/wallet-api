package org.dawvvlad.walletapp.data;

import jakarta.annotation.Nonnull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.util.UUID;

public record WalletOperationRequest(UUID uuid,
                                     OperationType operationType,
                                     @Nonnull
                                     @Positive (message = "amount must be positive")
                                     BigDecimal amount) {

};
