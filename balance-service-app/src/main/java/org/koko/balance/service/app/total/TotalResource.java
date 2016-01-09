package org.koko.balance.service.app.total;

import com.codahale.metrics.annotation.Timed;

import org.koko.balance.service.api.BalanceResponse;
import org.koko.balance.service.app.BalanceAppConfig;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * Balance total resource using akka actors
 */
@Path("/balances/total/{name}/akka")
public class TotalResource {

    private static final Logger log = LoggerFactory.getLogger(TotalResource.class);

    private final BalanceAppConfig appConfig;

    public TotalResource(BalanceAppConfig appConfig) {
        this.appConfig = appConfig;
    }

    @GET
    @Timed
    @Produces(MediaType.APPLICATION_JSON)
    public Response balanceTotal(@PathParam("name") String name) {

        log.debug("total balance akka get request [name:{}]", name);

        BalanceResponse response = new BalanceResponse(0L, name, 0L, "total balance");

        log.debug("total balance akka request served [name:{}]", name);

        return Response.ok().entity(response).build();
    }
}
