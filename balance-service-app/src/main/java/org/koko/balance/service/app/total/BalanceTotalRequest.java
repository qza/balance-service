package org.koko.balance.service.app.total;

/**
 * BalanceTotalRequest message
 */
public class BalanceTotalRequest {

    private final String name;

    private final String bank;

    public BalanceTotalRequest(String name) {
        this.name = name;
        this.bank = null;
    }

    public BalanceTotalRequest(String name, String bank) {
        this.name = name;
        this.bank = bank;
    }

    public String getName() {
        return name;
    }

    public String getBank() {
        return bank;
    }

    @Override
    public String toString() {
        return "BalanceTotalRequest [name: " + name + "] [ bank: " + bank + "]";
    }
}
