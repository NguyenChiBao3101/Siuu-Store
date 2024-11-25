package com.siuuuuu.backend.constant;

public enum PaymentStatus {
    /**
     * Payment has been made.
     */
    PAID,

    /**
     * Payment has not been made.
     */
    UNPAID,

    /**
     * Payment has failed.
     */
    FAILED,

    /**
     * Payment is pending.
     */
    PENDING
}