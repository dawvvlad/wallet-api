package org.dawvvlad.walletapp.repo;

import org.dawvvlad.walletapp.entity.Wallet;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

public interface CheckWalletRepository {

    /**
     * Проверяет существование кошелька и возвращает его с блокировкой
     */
    @Query("SELECT w FROM Wallet w WHERE w.uuid = :walletId")
    Optional<Wallet> findAndLockWallet(@Param("walletId") UUID walletId);

    /**
     * Проверяет существование кошелька
     */
    @Query("SELECT COUNT(w) > 0 FROM Wallet w WHERE w.uuid = :walletId")
    boolean existsWallet(@Param("walletId") UUID walletId);

    /**
     * Проверяет достаточно ли средств для операции
     */
    @Query("SELECT w.balance >= :amount FROM Wallet w WHERE w.uuid = :walletId")
    boolean hasSufficientFunds(@Param("walletId") UUID walletId,
                               @Param("amount") java.math.BigDecimal amount);

    /**
     * Проверяет, существует ли кошелек и достаточно ли средств
     * Возвращает кошелек, если все условия выполнены
     */
    @Query("SELECT w FROM Wallet w WHERE w.uuid = :walletId AND w.balance >= :amount")
    Optional<Wallet> findWithSufficientFunds(@Param("walletId") UUID walletId,
                                             @Param("amount") java.math.BigDecimal amount);
}