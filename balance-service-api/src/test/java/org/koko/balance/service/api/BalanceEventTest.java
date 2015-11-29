package org.koko.balance.service.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.dropwizard.jackson.Jackson;

import org.junit.Test;

import java.util.UUID;

import static io.dropwizard.testing.FixtureHelpers.fixture;
import static org.junit.Assert.assertEquals;

/**
 * Balance event test
 */
public class BalanceEventTest {

    ObjectMapper mapper = Jackson.newObjectMapper();

    String placeFixture = fixture("fixtures/event-mark-place.json");
    String takeFixture = fixture("fixtures/event-mark-take.json");

    BalanceEvent placeEvent = new BalanceEvent("mark", UUID.fromString("cffc0bdb-7a32-4598-8e4c-59f20d4ba411"), 100L, BalanceEvent.Type.PLACE);

    BalanceEvent takeEvent = new BalanceEvent("mark", UUID.fromString("21a82225-d138-4f94-8b88-1918420cd331"), 50L, BalanceEvent.Type.TAKE);

    @Test
    public void shouldSerializePlaceJson() throws Exception {
        assertEquals(placeFixture, mapper.writeValueAsString(placeEvent));
    }

    @Test
    public void shouldDeserializePlaceJson() throws Exception {
        assertEquals(placeEvent, mapper.readValue(placeFixture, BalanceEvent.class));
    }

    @Test
    public void shouldSerializeTakeJson() throws Exception {
        assertEquals(takeFixture, mapper.writeValueAsString(takeEvent));
    }

    @Test
    public void shouldDeserializeTakeJson() throws Exception {
        assertEquals(takeEvent, mapper.readValue(takeFixture, BalanceEvent.class));
    }

}
