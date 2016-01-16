package org.koko.balance.service.app.total;

/**
 * BalanceTotalRequest message
 */
public class BalanceTotalRequest {

    private final String name;

    private final String externalResource;

    public BalanceTotalRequest(String name) {
        this.name = name;
        this.externalResource = null;
    }

    public BalanceTotalRequest(String name, String externalResource) {
        this.name = name;
        this.externalResource = externalResource;
    }

    public String getName() {
        return name;
    }

    public String getExternalResource() {
        return externalResource;
    }
}
