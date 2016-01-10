package org.koko.balance.service.app.total;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.testkit.TestActorRef;
import org.junit.Test;

import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.JerseyTest;

import org.koko.balance.service.api.BalanceResponse;
import org.koko.balance.service.app.BalanceAppConfig;
import org.koko.balance.service.app.resources.BalanceExternalResource;

import javax.ws.rs.core.Application;
import javax.ws.rs.core.Response;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * TotalResource test class
 */
public class TotalResourceTest extends JerseyTest {

    BalanceAppConfig appConfig = mock(BalanceAppConfig.class);

    @Override
    protected Application configure() {
        ResourceConfig config = new ResourceConfig();
        ActorSystem testSystem = ActorSystem.create("BalanceTotalTestSystem");
        ActorRef balanceTotalWorker = testSystem.actorOf(new Props(TotalWorker.class));
        config.registerInstances(new TotalResource(balanceTotalWorker, appConfig), new BalanceExternalResource());
        return config;
    }

    @Test
    public void shouldCalculateTotal() throws Exception {

        when(appConfig.getBankUrlTemplate()).thenReturn("http://localhost:9998/balances/ext/{bank}/{name}");

        Response response = target("/balances/total/mark/akka").request().get();
        BalanceResponse balanceResponse = response.readEntity(BalanceResponse.class);

        assertEquals("mark", balanceResponse.getName());
    }

}