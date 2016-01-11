package org.koko.balance.service.app.total;

import akka.actor.UntypedActor;

/**
 * Balance total worker fetching balance from external http resources
 */
public class TotalWorkerActor extends UntypedActor {

    @Override
    public void onReceive(Object message) throws Exception {
        if (message instanceof TotalRequest) {
            getSender().tell(new TotalResponse((TotalRequest) message, 1L));
        }
    }

}