package org.koko.balance.service.app.total;

/**
 * Balance total worker actor exception
 */
public class TotalWorkerActorException extends RuntimeException {

    public TotalWorkerActorException() {
        // default constructor
    }

    public TotalWorkerActorException(String message) {
        super(message);
    }

    public TotalWorkerActorException(String message, Throwable cause) {
        super(message, cause);
    }
}
