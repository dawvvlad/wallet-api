package org.dawvvlad.walletapp.AOP;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.dawvvlad.walletapp.repo.CheckWalletRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.UUID;

@Aspect
@Component
@Slf4j
public class WalletValidationAspect {

    private final CheckWalletRepository checkWalletRepository;

    @Autowired
    public WalletValidationAspect(CheckWalletRepository checkWalletRepository) {
        this.checkWalletRepository = checkWalletRepository;
    }

    /**
     * Обработка аннотации @CheckWalletExists
     */
    @Around("@annotation(checkWalletExists)")
    public Object checkWalletExists(ProceedingJoinPoint joinPoint,
                                    CheckWalletExists checkWalletExists) throws Throwable {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        Object[] args = joinPoint.getArgs();
        String[] paramNames = signature.getParameterNames();

        UUID walletId = extractParameterValue(paramNames, args,
                checkWalletExists.walletIdParam(), UUID.class);

        if (walletId == null) {
            throw new IllegalArgumentException("Wallet ID parameter not found: " +
                    checkWalletExists.walletIdParam());
        }

        if (!checkWalletRepository.existsWallet(walletId)) {
            throw new IllegalArgumentException(checkWalletExists.message() + ": " + walletId);
        }

        log.debug("Wallet validation passed for walletId: {}", walletId);
        return joinPoint.proceed();
    }

    /**
     * Обработка аннотации @CheckBalanceFund
     */
    @Around("@annotation(checkBalanceFund)")
    public Object checkSufficientFunds(ProceedingJoinPoint joinPoint,
                                       CheckBalanceFund checkBalanceFund) throws Throwable {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Object[] args = joinPoint.getArgs();
        String[] paramNames = signature.getParameterNames();
        UUID walletId = extractParameterValue(paramNames, args,
                checkBalanceFund.walletIdParam(), UUID.class);
        BigDecimal amount = extractParameterValue(paramNames, args,
                checkBalanceFund.amountParam(), BigDecimal.class);

        if (walletId == null || amount == null) {
            throw new IllegalArgumentException("Required parameters not found");
        }
        if (!checkWalletRepository.hasSufficientFunds(walletId, amount)) {
            throw new IllegalArgumentException(checkBalanceFund.message() +
                    ". Wallet: " + walletId + ", Amount: " + amount);
        }

        log.debug("Balance funds validation passed for walletId: {}, amount: {}", walletId, amount);
        return joinPoint.proceed();
    }

    /**
     * Вспомогательный метод для извлечения значения параметра
     */
    @SuppressWarnings("unchecked")
    private <T> T extractParameterValue(String[] paramNames, Object[] args,
                                        String paramName, Class<T> type) {
        for (int i = 0; i < paramNames.length; i++) {
            if (paramNames[i].equals(paramName) && args[i] != null) {
                if (type.isInstance(args[i])) {
                    return (T) args[i];
                }
            }
        }
        return null;
    }
}
