package org.koko.balance.service.app.total;

import akka.actor.*;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import akka.routing.RoundRobinRouter;

import static akka.actor.SupervisorStrategy.stop;
import static akka.actor.SupervisorStrategy.escalate;
import static akka.actor.SupervisorStrategy.restart;

import okhttp3.OkHttpClient;

import org.koko.balance.service.app.BalanceAppConfig;

import scala.concurrent.util.Duration;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Balance total master actor collecting balance from workers and restarting worker actors in case of http server errors
 */
public class BalanceTotalMasterActor extends UntypedActor {

    private ActorRef listener; // ?

    private final BalanceAppConfig configuration;

    private final OkHttpClient client;

    private final LoggingAdapter log;

    private final List<String> externalResources;

    private final List<Long> balances;

    private final ActorRef workerRouter;

    private final SupervisorStrategy strategy;

    public BalanceTotalMasterActor(BalanceAppConfig configuration) {

        this.configuration = configuration;

        this.log = Logging.getLogger(getContext().system(), this);

        this.client = new OkHttpClient().newBuilder().readTimeout(5, TimeUnit.SECONDS).build();

        this.externalResources = Arrays.asList("bank1", "bank2", "bank3");

        this.balances = new ArrayList<>(externalResources.size());

        this.workerRouter = this.getContext()
                .actorOf(new Props(() -> new BalanceTotalWorkerActor(client, configuration, getSelf()))
                        .withRouter(new RoundRobinRouter(externalResources.size())), "workerRouter");

        this.strategy = new OneForOneStrategy(
                10, Duration.create(1, TimeUnit.MINUTES), // max 10 restarts in a minute, then stop
                (t) -> {
                    if (t instanceof BalanceTotalWorkerActorException) {
                        log.warning("restarting child actor", t);
                        return restart();
                    } else if (t instanceof IllegalArgumentException) {
                        log.error("illegal argument", t);
                        return stop();
                    } else {
                        log.error("escalating error", t);
                        return escalate();
                    }
                }
        );
    }

    @Override
    public void onReceive(Object message) throws Exception {

        if (message instanceof BalanceTotalRequest) {
            BalanceTotalRequest request = (BalanceTotalRequest) message;
            log.info("total request received {}", request);
            listener = getSender();
            externalResources.forEach((el) ->
                    workerRouter.tell(new BalanceTotalRequest(request.getName(), el), getSelf())
            );

        } else if (message instanceof BalanceTotalResponse) {
            log.info("total response received");
            BalanceTotalResponse response = (BalanceTotalResponse) message;
            balances.add(response.getTotal());
            if (balances.stream().filter(el -> el != null).count() == externalResources.size()) {
                Long total = balances.stream().reduce(0L, (x, y) -> x + y);
                listener.tell(new BalanceTotalResponse(response.getRequest(), total));
                log.info("total balance calculated: {}", total);
                getContext().stop(getSelf());
            }

        } else if (message instanceof BalanceTotalWorkerActorException) {
            log.info("actor restarted :: resending message");
            BalanceTotalWorkerActorException exception = (BalanceTotalWorkerActorException) message;
            workerRouter.tell(exception.getRequest(), getSelf());
        }
    }

    @Override
    public SupervisorStrategy supervisorStrategy() {
        return strategy;
    }

}