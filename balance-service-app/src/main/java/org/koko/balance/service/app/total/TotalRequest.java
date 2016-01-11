package org.koko.balance.service.app.total;

/**
 * TotalRequest message
 */
public class TotalRequest {

    private final String name;

    private final String externalResource;

    public TotalRequest(String name) {
        this.name = name;
        this.externalResource = null;
    }

    public TotalRequest(String name, String externalResource) {
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
