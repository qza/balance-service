package org.koko.balance.service.app.total;

/**
 * TotalResponse message
 */
public class TotalResponse {

    private final TotalRequest request;
    private final Long total;

    public TotalResponse(TotalRequest request, Long total) {
        this.request = request;
        this.total = total;
    }

    public TotalRequest getRequest() {
        return request;
    }

    public Long getTotal() {
        return total;
    }
}
