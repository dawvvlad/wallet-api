package org.dawvvlad.walletapp.service;

import org.dawvvlad.walletapp.data.WalletOperationRequest;

public interface WalletOperationService {
    void deposit(WalletOperationRequest request);
    void withdraw(WalletOperationRequest request);
}
