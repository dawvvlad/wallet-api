package org.dawvvlad.walletapp.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;

@RestController
@RequestMapping("/wallets")
public class GetBalanceController {

    @GetMapping("/{uuid}")
    public ResponseEntity<BigDecimal> getWallet(@PathVariable String uuid) {

        return uuid;
    }
}
