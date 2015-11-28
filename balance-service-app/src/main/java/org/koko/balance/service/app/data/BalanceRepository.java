package org.koko.balance.service.app.data;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * Dummy repository holding balance data
 */
public class BalanceRepository {

    private final Map<String, Long> balances = new HashMap<>();
    private final Random random = new Random();

    public BalanceRepository() {
        balances.put("tom", generateBalance());
        balances.put("mark", generateBalance());
        balances.put("john", generateBalance());
    }

    public Long get(String name) {
        return balances.get(name);
    }

    public boolean put(String name, Long balance) {
        boolean created = balances.get(name) == null;
        balances.put(name, balance);
        return created;
    }

    public boolean isAlive() {
        return true;
    }

    private long generateBalance() {
        return Math.abs(random.nextLong()) / 100;
    }

}
