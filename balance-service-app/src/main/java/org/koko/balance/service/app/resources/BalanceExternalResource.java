package org.koko.balance.service.app.resources;

import com.codahale.metrics.annotation.Timed;
import org.hibernate.validator.constraints.NotEmpty;
import org.koko.balance.service.api.BalanceResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

/**
 * External sources of balance information
 */
@Path("/balances/ext")
public class BalanceExternalResource {

    private static final Logger log = LoggerFactory.getLogger(BalanceExternalResource.class);

    private final AtomicLong bank1Counter = new AtomicLong();
    private final AtomicLong bank2Counter = new AtomicLong();

    @GET
    @Timed
    @Path("/bank1/{name}")
    @Produces(MediaType.APPLICATION_JSON)
    public BalanceResponse balanceBank1(@PathParam("name") @NotEmpty String name) {
        work("bank1 get balance request [name:{}]", name);
        return new BalanceResponse(bank1Counter.incrementAndGet(), name, Math.abs(new Random().nextLong() / 1000L), "");
    }

    @GET
    @Timed
    @Path("/bank2/{name}")
    @Produces(MediaType.APPLICATION_JSON)
    public BalanceResponse balanceBank2(@PathParam("name") @NotEmpty String name) {
        work("bank2 get balance request [name:{}]", name);
        return new BalanceResponse(bank2Counter.incrementAndGet(), name, Math.abs(new Random().nextLong() / 1000L), "");
    }

    private void work(String message, String... params) {
        try {
            log.info(message, params);
            TimeUnit.SECONDS.sleep(new Random().nextInt(5));
        } catch (InterruptedException e) {
            log.error("interrupted work", e);
        }
    }

}
