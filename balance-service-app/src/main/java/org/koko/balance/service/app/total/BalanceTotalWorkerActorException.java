package org.koko.balance.service.app.total;

/**
 * Balance total worker actor exception signaling requests that should be retried
 */
public class BalanceTotalWorkerActorException extends RuntimeException {

    private final BalanceTotalRequest request;

    public BalanceTotalWorkerActorException() {
        this.request = null;
    }

    public BalanceTotalWorkerActorException(String message) {
        super(message);
        this.request = null;
    }

    public BalanceTotalWorkerActorException(String message, Throwable cause) {
        super(message, cause);
        this.request = null;
    }

    public BalanceTotalWorkerActorException(String message, BalanceTotalRequest request) {
        super(message);
        this.request = request;
    }

    public BalanceTotalRequest getRequest() {
        return request;
    }
}
