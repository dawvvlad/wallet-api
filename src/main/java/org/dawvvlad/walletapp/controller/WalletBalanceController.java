package org.dawvvlad.walletapp.controller;

import org.dawvvlad.walletapp.service.WalletBalanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.UUID;

@RestController
@RequestMapping("/wallets")
public class WalletBalanceController {
    private final WalletBalanceService walletBalanceService;

    @Autowired
    public WalletBalanceController(WalletBalanceService walletBalanceService) {
        this.walletBalanceService = walletBalanceService;
    }

    @GetMapping("/{uuid}")
    public ResponseEntity<BigDecimal> getBalance(@PathVariable String uuid) {
        return ResponseEntity.ok(walletBalanceService.getBalance(UUID.fromString(uuid)));
    }
}
