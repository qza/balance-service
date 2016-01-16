package org.koko.balance.service.app.total;

import akka.actor.UntypedActor;

/**
 * Balance total worker fetching balance from external http resources
 */
public class BalanceTotalWorkerActor extends UntypedActor {

    @Override
    public void onReceive(Object message) throws Exception {
        if (message instanceof BalanceTotalRequest) {
            getSender().tell(new BalanceTotalResponse((BalanceTotalRequest) message, 1L));
        }
    }

}