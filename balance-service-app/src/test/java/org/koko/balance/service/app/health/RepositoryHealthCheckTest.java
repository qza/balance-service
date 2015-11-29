package org.koko.balance.service.app.health;

import org.koko.balance.service.app.data.BalanceRepository;

import org.junit.Test;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Repository health check test
 */
public class RepositoryHealthCheckTest {

    BalanceRepository repository = mock(BalanceRepository.class);

    RepositoryHealthCheck check = new RepositoryHealthCheck(repository);

    @Test
    public void shouldBeHealthy() throws Exception {
        when(repository.isAlive()).thenReturn(true);
        assertTrue(check.check().isHealthy());
    }

    @Test
    public void shouldNotBeHealthy() throws Exception {
        when(repository.isAlive()).thenReturn(false);
        assertFalse(check.check().isHealthy());
    }

}
