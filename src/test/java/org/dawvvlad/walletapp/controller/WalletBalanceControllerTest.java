package org.dawvvlad.walletapp.controller;

import org.dawvvlad.walletapp.service.WalletBalanceService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class WalletBalanceControllerTest {

    @Mock
    private WalletBalanceService walletBalanceService;

    @InjectMocks
    private WalletBalanceController walletBalanceController;

    private UUID testWalletId;
    private final String TEST_WALLET_UUID = "123e4567-e89b-12d3-a456-426614174000";

    @BeforeEach
    void setUp() {
        testWalletId = UUID.fromString(TEST_WALLET_UUID);
    }

    @Test
    void getBalance_ValidUuid_ShouldReturnBalance() {
        BigDecimal expectedBalance = new BigDecimal("1000.50");
        when(walletBalanceService.getBalance(testWalletId)).thenReturn(expectedBalance);
        ResponseEntity<BigDecimal> response = walletBalanceController.getBalance(TEST_WALLET_UUID);
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedBalance, response.getBody());
        verify(walletBalanceService, times(1)).getBalance(testWalletId);
    }

    @Test
    void getBalance_ValidUuidWithZeroBalance_ShouldReturnZero() {
        BigDecimal zeroBalance = BigDecimal.ZERO;
        when(walletBalanceService.getBalance(testWalletId)).thenReturn(zeroBalance);
        ResponseEntity<BigDecimal> response = walletBalanceController.getBalance(TEST_WALLET_UUID);
        assertEquals(zeroBalance, response.getBody());
    }

    @Test
    void getBalance_ValidUuidWithLargeBalance_ShouldReturnCorrectValue() {
        BigDecimal largeBalance = new BigDecimal("9999999999.9999");
        when(walletBalanceService.getBalance(testWalletId)).thenReturn(largeBalance);
        ResponseEntity<BigDecimal> response = walletBalanceController.getBalance(TEST_WALLET_UUID);
        assertEquals(largeBalance, response.getBody());
    }

    @Test
    void getBalance_InvalidUuidFormat_ShouldThrowException() {
        String invalidUuid = "invalid-uuid";
        assertThrows(IllegalArgumentException.class, () -> {
            walletBalanceController.getBalance(invalidUuid);
        });
    }

    @Test
    void getBalance_NullUuid_ShouldThrowException() {
        String nullUuid = null;
        assertThrows(NullPointerException.class, () -> {
            walletBalanceController.getBalance(nullUuid);
        });
    }

    @Test
    void getBalance_EmptyUuid_ShouldThrowException() {
        String emptyUuid = "";
        assertThrows(IllegalArgumentException.class, () -> {
            walletBalanceController.getBalance(emptyUuid);
        });
    }

    @Test
    void getBalance_ServiceThrowsException_ShouldPropagateException() {
        when(walletBalanceService.getBalance(testWalletId))
                .thenThrow(new RuntimeException("Wallet not found"));
        assertThrows(RuntimeException.class, () -> {
            walletBalanceController.getBalance(TEST_WALLET_UUID);
        });
    }

    @Test
    void getBalance_ServiceReturnsNull_ShouldReturnNullInResponse() {
        when(walletBalanceService.getBalance(testWalletId)).thenReturn(null);
        ResponseEntity<BigDecimal> response = walletBalanceController.getBalance(TEST_WALLET_UUID);
        assertNull(response.getBody());
        verify(walletBalanceService, times(1)).getBalance(testWalletId);
    }
}