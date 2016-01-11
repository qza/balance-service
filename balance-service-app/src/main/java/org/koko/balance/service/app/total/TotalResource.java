package org.koko.balance.service.app.total;

import akka.actor.ActorRef;
import akka.pattern.Patterns;

import scala.concurrent.Await;
import scala.concurrent.Future;
import scala.concurrent.util.Duration;

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
import java.util.concurrent.TimeUnit;

/**
 * Balance total resource delegating to actor system
 */
@Path("/balances/total/{name}/akka")
public class TotalResource {

    private static final Logger log = LoggerFactory.getLogger(TotalResource.class);

    private final BalanceAppConfig appConfig;
    private final ActorRef balanceTotalActor;

    public TotalResource(ActorRef balanceTotalActor, BalanceAppConfig appConfig) {
        this.balanceTotalActor = balanceTotalActor;
        this.appConfig = appConfig;
    }

    @GET
    @Timed
    @Produces(MediaType.APPLICATION_JSON)
    public Response balanceTotal(@PathParam("name") String name) throws Exception {

        log.info("total balance akka get request [name:{}]", name);

        Future<Object> resultFuture = Patterns.ask(balanceTotalActor, new TotalRequest("mark"), 1000);

        Object result = Await.result(resultFuture, Duration.create(10, TimeUnit.SECONDS));

        if(!(result instanceof TotalResponse)) {
            log.warn("unknown result [result:{}]", result);
            return Response.serverError().build();
        }

        TotalResponse totalResponse = (TotalResponse) result;
        log.debug("total balance akka request served [name:{}]", name);
        return Response.ok().entity(new BalanceResponse(0L, name, totalResponse.getTotal(), "total balance")).build();
    }
}
