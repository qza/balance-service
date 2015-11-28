package org.koko.balance.service.app.data;

import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * Balance repository test
 */
public class BalanceRepositoryTest {

    BalanceRepository repository = new BalanceRepository();

    @Test
    public void shouldBeAlive() {
        assertTrue(repository.isAlive());
    }

    @Test
    public void shouldGetExisting() {
        assertNotNull(repository.get("tom"));
    }

    @Test
    public void shouldUpdateExisting() {
        assertFalse(repository.put("tom", 0L));
    }

    @Test
    public void shouldAddNew() {
        assertTrue(repository.put("xyz", 0L));
    }

}
