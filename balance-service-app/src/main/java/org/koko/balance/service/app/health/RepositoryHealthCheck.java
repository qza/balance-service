package org.koko.balance.service.app.health;

import com.codahale.metrics.health.HealthCheck;
import org.koko.balance.service.app.data.BalanceRepository;

/**
 * Balance repository health check
 */
public class RepositoryHealthCheck extends HealthCheck {

    private final BalanceRepository repository;

    public RepositoryHealthCheck(BalanceRepository repository) {
        this.repository = repository;
    }

    @Override
    protected Result check() throws Exception {
        return repository.isAlive() ? Result.healthy() : Result.unhealthy("repository offline");
    }

}