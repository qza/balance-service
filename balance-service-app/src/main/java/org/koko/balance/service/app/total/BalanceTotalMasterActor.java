package org.koko.balance.service.app.total;

import akka.actor.*;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import akka.routing.RoundRobinRouter;
import scala.concurrent.util.Duration;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static akka.actor.SupervisorStrategy.escalate;
import static akka.actor.SupervisorStrategy.restart;

/**
 * Balance total master actor collecting balance from workers and restarting worker actors in case of http server errors
 */
public class BalanceTotalMasterActor extends UntypedActor {

    private final List<String> externalResources = Arrays.asList("bank1", "bank2", "bank3");

    private final LoggingAdapter log = Logging.getLogger(getContext().system(), this);

    private final ActorRef workerRouter = this.getContext().actorOf(new Props(BalanceTotalWorkerActor.class)
            .withRouter(new RoundRobinRouter(externalResources.size())), "workerRouter");

    private static final SupervisorStrategy strategy = new OneForOneStrategy(
            10, Duration.create(1, TimeUnit.MINUTES), // max 10 restarts in a minute, then stop
            (t) -> t instanceof BalanceTotalWorkerActorException ? restart() : escalate()
    );

    private final List<Long> balances = new ArrayList<>(externalResources.size());

    private ActorRef listener;

    @Override
    public SupervisorStrategy supervisorStrategy() {
        return strategy;
    }

    @Override
    public void onReceive(Object message) throws Exception {

        if (message instanceof BalanceTotalRequest) {
            log.info("total request received");
            listener = getSender();
            externalResources.forEach((el) -> workerRouter.tell(message, getSelf()));

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
        }
    }

}