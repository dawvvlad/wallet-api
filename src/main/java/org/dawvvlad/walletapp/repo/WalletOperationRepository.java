package org.dawvvlad.walletapp.repo;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * Репозиторий операций со счетом
 */
public interface WalletOperationRepository extends WalletRepository {
    @Transactional
    @Modifying
    @Query(value = """
        UPDATE wallet 
        SET balance = balance + :amount, 
            version = version + 1,
            updated_at = CURRENT_TIMESTAMP 
        WHERE id = :walletId
        """, nativeQuery = true)
    void deposit(@Param("walletId") UUID walletId, @Param("amount") BigDecimal amount);

    @Transactional
    @Modifying
    @Query(value = """
        UPDATE wallet 
        SET balance = balance - :amount, 
            version = version + 1,
            updated_at = CURRENT_TIMESTAMP 
        WHERE id = :walletId AND balance >= :amount
        """, nativeQuery = true)
    void withdraw(@Param("walletId") UUID walletId, @Param("amount") BigDecimal amount);
}
