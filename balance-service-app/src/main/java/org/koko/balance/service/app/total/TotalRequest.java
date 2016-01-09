package org.koko.balance.service.app.total;

/**
 * TotalRequest message
 */
public class TotalRequest {

    private String name;

    public TotalRequest(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

}
