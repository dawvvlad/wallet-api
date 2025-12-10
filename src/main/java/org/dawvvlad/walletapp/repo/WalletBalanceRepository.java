package org.dawvvlad.walletapp.repo;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * Репозиторий для баланса
 */
public interface WalletBalanceRepository extends WalletRepository {
    @Query("SELECT w.balance FROM Wallet w WHERE w.uuid = :walletId")
    BigDecimal findBalanceByWalletId(@Param("walletId") UUID walletId);
}
