package org.dawvvlad.walletapp.controller;

import jakarta.validation.Valid;
import org.dawvvlad.walletapp.AOP.CheckBalanceFund;
import org.dawvvlad.walletapp.AOP.CheckWalletExists;
import org.dawvvlad.walletapp.data.WalletOperationRequest;
import org.dawvvlad.walletapp.service.WalletOperationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/wallet")
public class WalletOperationController {

    private final WalletOperationService walletOperationService;

    @Autowired
    public WalletOperationController(WalletOperationService walletOperationService) {
        this.walletOperationService = walletOperationService;
    }

    @PostMapping("/deposit")
    @CheckWalletExists(walletIdParam = "request.uuid")
    public ResponseEntity<?> deposit(@Valid WalletOperationRequest walletOperationRequest) {
        walletOperationService.deposit(walletOperationRequest);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/withdraw")
    @CheckWalletExists(walletIdParam = "request.uuid")
    @CheckBalanceFund(walletIdParam = "request.uuid", amountParam = "request.amount")
    public ResponseEntity<?> withdraw(@Valid WalletOperationRequest walletOperationRequest) {
        walletOperationService.withdraw(walletOperationRequest);
        return ResponseEntity.ok().build();
    }
}
