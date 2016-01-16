package org.koko.balance.service.app.total;

/**
 * Balance total worker actor exception signaling requests that should be retried
 */
public class BalanceTotalWorkerActorException extends RuntimeException {

    public BalanceTotalWorkerActorException() {
        // default constructor
    }

    public BalanceTotalWorkerActorException(String message) {
        super(message);
    }

    public BalanceTotalWorkerActorException(String message, Throwable cause) {
        super(message, cause);
    }
}
