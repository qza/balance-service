package org.koko.balance.service.app.resources;

import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.JerseyTest;
import org.koko.balance.service.api.BalanceResponse;
import org.koko.balance.service.app.BalanceAppConfig;

import javax.ws.rs.core.Application;
import javax.ws.rs.core.Response;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

/**
 * Balance collector resource test
 */
public class BalanceCollectorResourceTest extends JerseyTest {

    static BalanceAppConfig appConfig = mock(BalanceAppConfig.class);

    @Override
    protected Application configure() {
        ResourceConfig config = new ResourceConfig();
        config.registerInstances(new BalanceCollectorResource(appConfig), new BalanceExternalResource());
        return config;
    }

    @Test
    public void shouldCollectBalances() throws Exception {

        when(appConfig.getBankUrlTemplate()).thenReturn("http://localhost:9998/balances/ext/{bank}/{name}");

        Response response = target("/balances/total/mark").request().get();
        BalanceResponse balanceResponse = response.readEntity(BalanceResponse.class);

        assertEquals("mark", balanceResponse.getName());
    }

}
