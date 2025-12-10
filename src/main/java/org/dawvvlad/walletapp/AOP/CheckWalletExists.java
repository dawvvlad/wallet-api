package org.dawvvlad.walletapp.AOP;

import java.lang.annotation.*;

/**
 * Аннотация для проверки существования кошелька
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface CheckWalletExists {
    String walletIdParam() default "walletId";
    String message() default "Wallet not found";
}
