package org.dawvvlad.walletapp.AOP;

import java.lang.annotation.*;

/**
 * Аннотация для проверки достаточности средств
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface CheckBalanceFund {
    String walletIdParam() default "walletId";
    String amountParam() default "amount";
    String message() default "Insufficient funds";
}
