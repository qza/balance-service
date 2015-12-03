package org.koko.balance.service.app.resources;

import com.codahale.metrics.annotation.Timed;
import org.hibernate.validator.constraints.NotEmpty;
import org.koko.balance.service.api.BalanceResponse;

import org.koko.balance.service.app.data.Randomised;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * External bank resources. Resource is occasionally not available
 */
@Path("/balances/ext")
public class BalanceExternalResource {

    private static final Logger log = LoggerFactory.getLogger(BalanceExternalResource.class);

    @GET
    @Timed
    @Path("/{bank}/{name}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response balance(@PathParam("bank") @NotEmpty String bank, @PathParam("name") @NotEmpty String name) {

        BalanceResponse balanceEntity;
        Response.Status status = Randomised.httpOkNotAvailable();

        if (status.getFamily().equals(Response.Status.Family.SUCCESSFUL)) {
            log.info("{} - balance request received [name:{}]", bank, name);
            try {
                TimeUnit.SECONDS.sleep(new Random().nextInt(5));
            } catch (InterruptedException e) {
                Thread.interrupted();
            }
            balanceEntity = new BalanceResponse(0L, name, Math.abs(new Random().nextLong() / 1000L), "ok");
        } else {
            balanceEntity = new BalanceResponse(0L, name, 0L, "error occurred");
        }

        return Response.status(status.getStatusCode()).entity(balanceEntity).build();
    }

}
