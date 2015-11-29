package org.koko.balance.service.app;

import static org.mockito.Mockito.*;

import com.codahale.metrics.health.HealthCheckRegistry;
import io.dropwizard.jersey.setup.JerseyEnvironment;
import io.dropwizard.setup.AdminEnvironment;
import io.dropwizard.setup.Environment;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import org.koko.balance.service.app.resources.BalanceResource;

/**
 * Balance app test
 */
public class BalanceAppTest {

    Environment environment = mock(Environment.class);
    JerseyEnvironment jersey = mock(JerseyEnvironment.class);
    AdminEnvironment admin = mock(AdminEnvironment.class);
    HealthCheckRegistry healths = mock(HealthCheckRegistry.class);

    BalanceApp app = new BalanceApp();
    BalanceAppConfig config = new BalanceAppConfig();

    @Before
    public void setup() throws Exception {
        config.setBalanceMessageTemplate("Current balance is: %s");
        when(environment.jersey()).thenReturn(jersey);
        when(environment.healthChecks()).thenReturn(healths);
        when(environment.admin()).thenReturn(admin);
    }

    @After
    public void tearDown() throws Exception {
        reset(environment, jersey, admin, healths);
    }

    @Test
    public void buildsAThingResource() throws Exception {
        app.run(config, environment);
        verify(jersey).register(isA(BalanceResource.class));
    }

}
