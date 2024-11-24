package com.siuuuuu.backend.constant;

public enum OrderStatus {
    /**
     * Order is pending and awaiting further action.
     */
    PENDING,

    /**
     * Order has been confirmed.
     */
    CONFIRMED,

    /**
     * Order is currently being shipped.
     */
    SHIPPING,

    /**
     * Order has been completed.
     */
    COMPLETED,

    /**
     * Order has been cancelled.
     */
    CANCELLED
}