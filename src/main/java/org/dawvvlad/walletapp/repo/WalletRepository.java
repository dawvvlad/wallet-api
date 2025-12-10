package org.dawvvlad.walletapp.repo;

import org.dawvvlad.walletapp.entity.Wallet;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

/**
 * Базовый репозиторий
 */
public interface WalletRepository extends JpaRepository<Wallet, UUID> {

}
