package org.dawvvlad.walletapp.service.impl;

import org.dawvvlad.walletapp.data.OperationType;
import org.dawvvlad.walletapp.data.WalletOperationRequest;
import org.dawvvlad.walletapp.repo.WalletOperationRepository;
import org.dawvvlad.walletapp.service.WalletOperationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class WalletOperationServiceImpl implements WalletOperationService {

    private final WalletOperationRepository walletOperationRepository;

    @Autowired
    public WalletOperationServiceImpl(WalletOperationRepository walletOperationRepository) {
        this.walletOperationRepository = walletOperationRepository;
    }

    @Override
    public void deposit(WalletOperationRequest request) {
        if(!request.operationType().equals(OperationType.DEPOSIT)) throw new IllegalArgumentException("Only deposit operations are supported");
        walletOperationRepository.deposit(request.uuid(), request.amount());
    }

    @Override
    public void withdraw(WalletOperationRequest request) {
        if(!request.operationType().equals(OperationType.WITHDRAW)) throw new IllegalArgumentException("Only deposit operations are supported");
        walletOperationRepository.withdraw(request.uuid(), request.amount());
    }
}
