package org.dawvvlad.walletapp.repo;

import org.dawvvlad.walletapp.entity.Wallet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

public interface WalletBalanceRepository extends WalletOperationRepository, JpaRepository<Wallet, UUID> {
    @Query("SELECT w.balance FROM Wallet w WHERE w.uuid = :walletId")
    Optional<BigDecimal> findBalanceByWalletId(@Param("walletId") UUID walletId);
}
