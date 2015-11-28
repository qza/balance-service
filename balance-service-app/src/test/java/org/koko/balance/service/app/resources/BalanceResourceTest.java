package org.koko.balance.service.app.resources;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.After;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;

import io.dropwizard.testing.junit.ResourceTestRule;
import org.koko.balance.service.app.data.BalanceRepository;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.net.URLEncoder;

/**
 * Balance resource test class
 */
public class BalanceResourceTest {

    private static final BalanceRepository repository = mock(BalanceRepository.class);

    @ClassRule
    public static final ResourceTestRule resources = ResourceTestRule.builder()
            .addResource(new BalanceResource("Balance is: %s", repository))
            .build();

    @Before
    public void setup() {
        when(repository.get(eq("xxx"))).thenReturn(null);
        when(repository.get(eq("tom"))).thenReturn(10L);
        when(repository.put(eq("john"), anyLong())).thenReturn(true);
        when(repository.put(eq("mark"), anyLong())).thenReturn(false);
    }

    @After
    public void tearDown(){
        reset(repository);
    }

    @Test
    public void shouldGetNotFoundForUnknownName() throws Exception {
        Response response = resources.client().target("/balances/xxx").request().get();
        assertEquals(404, response.getStatus());
        verify(repository).get("xxx");
    }

    @Test
    public void shouldGetBalance() {
        Response response = resources.client().target("/balances/tom").request().get();
        assertEquals(200, response.getStatus());
        verify(repository).get("tom");
    }

    @Test
    public void shouldCreateOnFirstPut() throws Exception {
        String balance = URLEncoder.encode("$100,000.00", "UTF-8");
        Entity<String> defaultEntity = Entity.entity("", MediaType.APPLICATION_JSON);
        Response response = resources.client().target("/balances/john").queryParam("balance",balance).request().put(defaultEntity);
        assertEquals(201, response.getStatus());
        verify(repository).put(eq("john"), anyLong());
    }

    @Test
    public void shouldUpdateOnFollowingPut() throws Exception {
        String balance = URLEncoder.encode("$200,000.00", "UTF-8");
        Entity<String> defaultEntity = Entity.entity("", MediaType.APPLICATION_JSON);
        Response response = resources.client().target("/balances/mark").queryParam("balance",balance).request().put(defaultEntity);
        assertEquals(200, response.getStatus());
        verify(repository).put(eq("mark"), anyLong());
    }

}
