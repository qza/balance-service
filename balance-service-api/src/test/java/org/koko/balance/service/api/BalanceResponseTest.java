package org.koko.balance.service.api;

import static io.dropwizard.testing.FixtureHelpers.fixture;
import static org.junit.Assert.assertEquals;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.dropwizard.jackson.Jackson;

import org.junit.Test;

/**
 * Balance response representation test
 */
public class BalanceResponseTest {

    ObjectMapper mapper = Jackson.newObjectMapper();

    String fixture = fixture("fixtures/response-mark.json");

    BalanceResponse entity = new BalanceResponse(1L, "mark", 10000L, "Current balance is $10,000.00");

    @Test
    public void shouldSerializesJson() throws Exception {
        assertEquals(fixture, mapper.writeValueAsString(entity));
    }

    @Test
    public void shouldDeserializeJson() throws Exception {
        assertEquals(entity, mapper.readValue(fixture, BalanceResponse.class));
    }

}
