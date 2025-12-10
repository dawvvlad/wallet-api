package org.dawvvlad.walletapp.service.impl;

import org.dawvvlad.walletapp.repo.WalletBalanceRepository;
import org.dawvvlad.walletapp.service.WalletBalanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.UUID;

@Service
@Transactional
public class WalletBalanceServiceImpl implements WalletBalanceService {

    private final WalletBalanceRepository walletBalanceRepository;

    @Autowired
    public WalletBalanceServiceImpl(WalletBalanceRepository walletBalanceRepository) {
        this.walletBalanceRepository = walletBalanceRepository;
    }

    @Override
    public BigDecimal getBalance(UUID uuid) {
        return walletBalanceRepository.findBalanceByWalletId(uuid);
    }
}
