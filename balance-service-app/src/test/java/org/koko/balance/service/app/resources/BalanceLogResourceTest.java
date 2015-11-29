package org.koko.balance.service.app.resources;

import io.dropwizard.testing.junit.ResourceTestRule;
import org.koko.balance.service.app.data.BalanceLogPath;

import javax.ws.rs.core.Response;

import org.junit.ClassRule;
import org.junit.Test;

import java.nio.file.Files;

import static org.junit.Assert.assertEquals;

/**
 * Balance log resource test
 */
public class BalanceLogResourceTest {

    public static final BalanceLogPath PATH = new BalanceLogPath("src/test/resources/balance_log");

    @ClassRule
    public static final ResourceTestRule resources = ResourceTestRule.builder()
            .addResource(new BalanceLogResource(PATH))
            .build();

    @Test
    public void shouldGetBalanceLog() throws Exception {
        if(Files.exists(PATH.getPath())) {
            Files.delete(PATH.getPath());
        }
        Files.createFile(PATH.getPath());
        Response response = resources.client().target("/balances/log").request().get();
        assertEquals(200, response.getStatus());
    }

}
