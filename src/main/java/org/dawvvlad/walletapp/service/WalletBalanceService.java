package org.dawvvlad.walletapp.service;

import java.math.BigDecimal;
import java.util.UUID;

public interface WalletBalanceService {
    BigDecimal getBalance(UUID uuid);
}
