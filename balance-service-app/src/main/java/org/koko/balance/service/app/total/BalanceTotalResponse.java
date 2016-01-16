package org.koko.balance.service.app.total;

/**
 * BalanceTotalResponse message
 */
public class BalanceTotalResponse {

    private final BalanceTotalRequest request;
    private final Long total;

    public BalanceTotalResponse(BalanceTotalRequest request, Long total) {
        this.request = request;
        this.total = total;
    }

    public BalanceTotalRequest getRequest() {
        return request;
    }

    public Long getTotal() {
        return total;
    }
}
