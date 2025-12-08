package org.dawvvlad.walletapp.controller;

import jakarta.validation.Valid;
import org.dawvvlad.walletapp.data.WalletOperationRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/wallet")
public class WalletOperationController {

    @PostMapping
    public ResponseEntity<?> doOperation(@Valid WalletOperationRequest walletOperationRequest) {
        return ResponseEntity.ok().build();
    }
}
