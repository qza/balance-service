package org.koko.balance.service.app.resources;

import io.dropwizard.testing.junit.ResourceTestRule;

import javax.ws.rs.core.Response;

import org.junit.Test;
import org.junit.ClassRule;

import static org.junit.Assert.assertNotNull;

/**
 * Balance external resource test
 */
public class BalanceExternalResourceTest {

    @ClassRule
    public static final ResourceTestRule resources = ResourceTestRule.builder()
            .addResource(new BalanceExternalResource())
            .build();

    @Test
    public void shouldGetBalanceFromBank1() {
        Response response = resources.client().target("/balances/ext/bank1/tom").request().get();
        assertNotNull(response);
    }

    @Test
    public void shouldGetBalanceFromBank2() {
        Response response = resources.client().target("/balances/ext/bank2/tom").request().get();
        assertNotNull(response);
    }
}
