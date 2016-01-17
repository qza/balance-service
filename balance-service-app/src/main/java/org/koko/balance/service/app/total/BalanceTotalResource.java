package org.koko.balance.service.app.total;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
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
import java.util.Optional;
import java.util.concurrent.TimeUnit;

/**
 * Balance total resource delegating to actor system
 */
@Path("/balances/total/akka/{name}")
public class BalanceTotalResource {

    private static final Logger log = LoggerFactory.getLogger(BalanceTotalResource.class);

    private final Duration timeout = Duration.create(1, TimeUnit.MINUTES);

    private final BalanceAppConfig appConfig;
    private final ActorRef balanceTotalActorDefault;

    public BalanceTotalResource(ActorRef balanceTotalActorDefault, BalanceAppConfig appConfig) {
        this.balanceTotalActorDefault = balanceTotalActorDefault;
        this.appConfig = appConfig;
    }

    @GET
    @Timed
    @Produces(MediaType.APPLICATION_JSON)
    public Response balanceTotal(@PathParam("name") String name) throws Exception {

        log.info("total balance akka get request [name:{}]", name);

        ActorRef balanceTotalActor = Optional.ofNullable(balanceTotalActorDefault)
                .orElse(ActorSystem.create("balance-total-actor-system").actorOf(
                        new Props(() -> new BalanceTotalMasterActor(appConfig)))
                );

        Future<Object> resultFuture = Patterns.ask(balanceTotalActor, new BalanceTotalRequest(name), timeout.toMillis());

        Object result = Await.result(resultFuture, timeout);

        if (!(result instanceof BalanceTotalResponse)) {
            log.warn("unknown result [result:{}]", result);
            return Response.serverError().build();
        }

        BalanceTotalResponse balanceTotalResponse = (BalanceTotalResponse) result;
        log.debug("total balance akka request served [name:{}]", name);
        return Response.ok().entity(new BalanceResponse(0L, name, balanceTotalResponse.getTotal(), "total balance")).build();
    }

}
