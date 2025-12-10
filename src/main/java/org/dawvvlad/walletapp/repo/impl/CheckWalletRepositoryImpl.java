package org.dawvvlad.walletapp.repo.impl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.LockModeType;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.dawvvlad.walletapp.entity.Wallet;
import org.dawvvlad.walletapp.repo.CheckWalletRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class CheckWalletRepositoryImpl implements CheckWalletRepository {

    @PersistenceContext
    private final EntityManager entityManager;

    @Override
    @Transactional(readOnly = true)
    public Optional<Wallet> findAndLockWallet(UUID walletId) {
        return Optional.ofNullable(
                entityManager.find(Wallet.class, walletId, LockModeType.PESSIMISTIC_WRITE)
        );
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsWallet(UUID walletId) {
        Long count = entityManager.createQuery(
                        "SELECT COUNT(w) FROM Wallet w WHERE w.uuid = :walletId", Long.class)
                .setParameter("walletId", walletId)
                .getSingleResult();
        return count > 0;
    }

    @Override
    @Transactional(readOnly = true)
    public boolean hasSufficientFunds(UUID walletId, BigDecimal amount) {
        return entityManager.createQuery(
                        "SELECT w.balance >= :amount FROM Wallet w WHERE w.uuid = :walletId", Boolean.class)
                .setParameter("walletId", walletId)
                .setParameter("amount", amount)
                .getResultStream()
                .findFirst()
                .orElse(false);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Wallet> findWithSufficientFunds(UUID walletId, BigDecimal amount) {
        return entityManager.createQuery(
                        "SELECT w FROM Wallet w WHERE w.uuid = :walletId AND w.balance >= :amount", Wallet.class)
                .setParameter("walletId", walletId)
                .setParameter("amount", amount)
                .getResultStream()
                .findFirst();
    }
}