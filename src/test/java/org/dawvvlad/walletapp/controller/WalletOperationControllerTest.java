package org.dawvvlad.walletapp.controller;

import org.dawvvlad.walletapp.data.WalletOperationRequest;
import org.dawvvlad.walletapp.data.OperationType;
import org.dawvvlad.walletapp.service.WalletOperationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class WalletOperationControllerTest {

    @Mock
    private WalletOperationService walletOperationService;

    @InjectMocks
    private WalletOperationController walletOperationController;

    @Captor
    private ArgumentCaptor<WalletOperationRequest> requestCaptor;

    private WalletOperationRequest validDepositRequest;
    private WalletOperationRequest validWithdrawRequest;
    private UUID testWalletId;
    private final String TEST_WALLET_UUID = "123e4567-e89b-12d3-a456-426614174000";

    @BeforeEach
    void setUp() {
        testWalletId = UUID.fromString(TEST_WALLET_UUID);

        validDepositRequest = new WalletOperationRequest(
                testWalletId,
                OperationType.DEPOSIT,
                new BigDecimal("1000.00")
        );

        validWithdrawRequest = new WalletOperationRequest(
                testWalletId,
                OperationType.WITHDRAW,
                new BigDecimal("500.00")
        );
    }

    @Test
    void deposit_ValidRequest_ShouldReturnOk() {
        // Arrange
        doNothing().when(walletOperationService).deposit(any(WalletOperationRequest.class));

        // Act
        ResponseEntity<?> response = walletOperationController.deposit(validDepositRequest);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNull(response.getBody());

        verify(walletOperationService, times(1)).deposit(requestCaptor.capture());
        WalletOperationRequest capturedRequest = requestCaptor.getValue();

        assertEquals(validDepositRequest.uuid(), capturedRequest.uuid());
        assertEquals(validDepositRequest.operationType(), capturedRequest.operationType());
        assertEquals(validDepositRequest.amount(), capturedRequest.amount());
    }

    @Test
    void deposit_ServicePerformsSuccessfully_ShouldReturnOk() {
        // Arrange
        doNothing().when(walletOperationService).deposit(validDepositRequest);

        // Act
        ResponseEntity<?> response = walletOperationController.deposit(validDepositRequest);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(walletOperationService, times(1)).deposit(validDepositRequest);
    }

    @Test
    void withdraw_ValidRequest_ShouldReturnOk() {
        // Arrange
        doNothing().when(walletOperationService).withdraw(any(WalletOperationRequest.class));

        // Act
        ResponseEntity<?> response = walletOperationController.withdraw(validWithdrawRequest);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNull(response.getBody());

        verify(walletOperationService, times(1)).withdraw(requestCaptor.capture());
        WalletOperationRequest capturedRequest = requestCaptor.getValue();

        assertEquals(validWithdrawRequest.uuid(), capturedRequest.uuid());
        assertEquals(validWithdrawRequest.operationType(), capturedRequest.operationType());
        assertEquals(validWithdrawRequest.amount(), capturedRequest.amount());
    }

    @Test
    void withdraw_ServicePerformsSuccessfully_ShouldReturnOk() {
        // Arrange
        doNothing().when(walletOperationService).withdraw(validWithdrawRequest);

        // Act
        ResponseEntity<?> response = walletOperationController.withdraw(validWithdrawRequest);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(walletOperationService, times(1)).withdraw(validWithdrawRequest);
    }

    @Test
    void deposit_WithNullRequest_ShouldThrowNullPointerException() {
        // Arrange
        WalletOperationRequest nullRequest = null;

        // Act & Assert
        assertThrows(NullPointerException.class, () -> {
            walletOperationController.deposit(nullRequest);
        });

        verify(walletOperationService, never()).deposit(any());
    }

    @Test
    void withdraw_WithNullRequest_ShouldThrowNullPointerException() {
        // Arrange
        WalletOperationRequest nullRequest = null;

        // Act & Assert
        assertThrows(NullPointerException.class, () -> {
            walletOperationController.withdraw(nullRequest);
        });

        verify(walletOperationService, never()).withdraw(any());
    }

    @Test
    void deposit_ServiceThrowsRuntimeException_ShouldPropagateException() {
        // Arrange
        doThrow(new RuntimeException("Service error"))
                .when(walletOperationService).deposit(any(WalletOperationRequest.class));

        // Act & Assert
        assertThrows(RuntimeException.class, () -> {
            walletOperationController.deposit(validDepositRequest);
        });

        verify(walletOperationService, times(1)).deposit(validDepositRequest);
    }

    @Test
    void withdraw_ServiceThrowsRuntimeException_ShouldPropagateException() {
        // Arrange
        doThrow(new RuntimeException("Service error"))
                .when(walletOperationService).withdraw(any(WalletOperationRequest.class));

        // Act & Assert
        assertThrows(RuntimeException.class, () -> {
            walletOperationController.withdraw(validWithdrawRequest);
        });

        verify(walletOperationService, times(1)).withdraw(validWithdrawRequest);
    }

    @Test
    void deposit_WithZeroAmount_ShouldCallService() {
        // Arrange
        WalletOperationRequest zeroAmountRequest = new WalletOperationRequest(
                testWalletId,
                OperationType.DEPOSIT,
                BigDecimal.ZERO
        );
        doNothing().when(walletOperationService).deposit(any(WalletOperationRequest.class));

        // Act
        ResponseEntity<?> response = walletOperationController.deposit(zeroAmountRequest);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(walletOperationService, times(1)).deposit(zeroAmountRequest);
    }

    @Test
    void withdraw_WithZeroAmount_ShouldCallService() {
        // Arrange
        WalletOperationRequest zeroAmountRequest = new WalletOperationRequest(
                testWalletId,
                OperationType.WITHDRAW,
                BigDecimal.ZERO
        );
        doNothing().when(walletOperationService).withdraw(any(WalletOperationRequest.class));

        // Act
        ResponseEntity<?> response = walletOperationController.withdraw(zeroAmountRequest);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(walletOperationService, times(1)).withdraw(zeroAmountRequest);
    }

    @Test
    void deposit_WithNegativeAmount_ShouldCallService() {
        // Arrange
        WalletOperationRequest negativeAmountRequest = new WalletOperationRequest(
                testWalletId,
                OperationType.DEPOSIT,
                new BigDecimal("-100.00")
        );
        doNothing().when(walletOperationService).deposit(any(WalletOperationRequest.class));

        // Act
        ResponseEntity<?> response = walletOperationController.deposit(negativeAmountRequest);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(walletOperationService, times(1)).deposit(negativeAmountRequest);
    }

    @Test
    void withdraw_WithNegativeAmount_ShouldCallService() {
        // Arrange
        WalletOperationRequest negativeAmountRequest = new WalletOperationRequest(
                testWalletId,
                OperationType.WITHDRAW,
                new BigDecimal("-100.00")
        );
        doNothing().when(walletOperationService).withdraw(any(WalletOperationRequest.class));

        // Act
        ResponseEntity<?> response = walletOperationController.withdraw(negativeAmountRequest);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(walletOperationService, times(1)).withdraw(negativeAmountRequest);
    }

    @Test
    void deposit_WithoutOptionalFields_ShouldCallService() {
        // Arrange
        WalletOperationRequest minimalRequest = new WalletOperationRequest(
                testWalletId,
                OperationType.DEPOSIT,
                new BigDecimal("100.00")
        );
        doNothing().when(walletOperationService).deposit(any(WalletOperationRequest.class));

        // Act
        ResponseEntity<?> response = walletOperationController.deposit(minimalRequest);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(walletOperationService, times(1)).deposit(minimalRequest);
    }

    @Test
    void withdraw_WithoutOptionalFields_ShouldCallService() {
        // Arrange
        WalletOperationRequest minimalRequest = new WalletOperationRequest(
                testWalletId,
                OperationType.WITHDRAW,
                new BigDecimal("50.00")
        );
        doNothing().when(walletOperationService).withdraw(any(WalletOperationRequest.class));

        // Act
        ResponseEntity<?> response = walletOperationController.withdraw(minimalRequest);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(walletOperationService, times(1)).withdraw(minimalRequest);
    }

    @Test
    void deposit_WithMaxDecimalPrecision_ShouldCallService() {
        // Arrange
        WalletOperationRequest maxPrecisionRequest = new WalletOperationRequest(
                testWalletId,
                OperationType.DEPOSIT,
                new BigDecimal("999999999999999.9999")
        );
        doNothing().when(walletOperationService).deposit(any(WalletOperationRequest.class));

        // Act
        ResponseEntity<?> response = walletOperationController.deposit(maxPrecisionRequest);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(walletOperationService, times(1)).deposit(maxPrecisionRequest);
    }

    @Test
    void withdraw_WithMaxDecimalPrecision_ShouldCallService() {
        // Arrange
        WalletOperationRequest maxPrecisionRequest = new WalletOperationRequest(
                testWalletId,
                OperationType.WITHDRAW,
                new BigDecimal("999999999999999.9999")
        );
        doNothing().when(walletOperationService).withdraw(any(WalletOperationRequest.class));

        // Act
        ResponseEntity<?> response = walletOperationController.withdraw(maxPrecisionRequest);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(walletOperationService, times(1)).withdraw(maxPrecisionRequest);
    }

    @Test
    void deposit_AndWithdraw_SequentialCalls_ShouldWorkCorrectly() {
        // Arrange
        doNothing().when(walletOperationService).deposit(any(WalletOperationRequest.class));
        doNothing().when(walletOperationService).withdraw(any(WalletOperationRequest.class));

        // Act
        ResponseEntity<?> depositResponse = walletOperationController.deposit(validDepositRequest);
        ResponseEntity<?> withdrawResponse = walletOperationController.withdraw(validWithdrawRequest);

        // Assert
        assertEquals(HttpStatus.OK, depositResponse.getStatusCode());
        assertEquals(HttpStatus.OK, withdrawResponse.getStatusCode());

        verify(walletOperationService, times(1)).deposit(validDepositRequest);
        verify(walletOperationService, times(1)).withdraw(validWithdrawRequest);
    }

    @Test
    void deposit_ServiceReturnsVoid_ShouldNotAffectResponse() {
        // Arrange
        doNothing().when(walletOperationService).deposit(validDepositRequest);

        // Act
        ResponseEntity<?> response = walletOperationController.deposit(validDepositRequest);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    void withdraw_ServiceReturnsVoid_ShouldNotAffectResponse() {
        // Arrange
        doNothing().when(walletOperationService).withdraw(validWithdrawRequest);

        // Act
        ResponseEntity<?> response = walletOperationController.withdraw(validWithdrawRequest);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNull(response.getBody());
    }
}