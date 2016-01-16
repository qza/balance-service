package org.koko.balance.service.app.total;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import org.junit.Test;

import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.JerseyTest;

import org.koko.balance.service.api.BalanceResponse;
import org.koko.balance.service.app.BalanceAppConfig;
import org.koko.balance.service.app.resources.BalanceExternalResource;

import javax.ws.rs.core.Application;
import javax.ws.rs.core.Response;

import static org.junit.Assert.assertEquals;

/**
 * BalanceTotalResource test class
 */
public class BalanceTotalResourceTest extends JerseyTest {

    @Override
    protected Application configure() {
        BalanceAppConfig appConfig = new BalanceAppConfig();
        appConfig.setBankUrlTemplate("http://localhost:9998/balances/ext/{bank}/{name}");
        ResourceConfig config = new ResourceConfig();
        ActorSystem testSystem = ActorSystem.create("BalanceTotalTestSystem");
        ActorRef balanceTotalActor = testSystem.actorOf(new Props(() -> new BalanceTotalMasterActor(appConfig)));
        config.registerInstances(new BalanceTotalResource(balanceTotalActor, appConfig), new BalanceExternalResource());
        return config;
    }

    @Test
    public void shouldCalculateTotal() throws Exception {
        Response response = target("/balances/total/akka/mark").request().get();
        BalanceResponse balanceResponse = response.readEntity(BalanceResponse.class);

        assertEquals("mark", balanceResponse.getName());
        assertEquals(Long.valueOf(3), balanceResponse.getBalance());
    }

}