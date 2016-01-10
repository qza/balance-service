package org.koko.balance.service.app.total;

import akka.actor.UntypedActor;

/**
 * Balance total worker
 */
public class TotalWorker extends UntypedActor {

    @Override
    public void onReceive(Object message) throws Exception {
        if (message instanceof TotalRequest) {
            getSender().tell(new TotalResponse((TotalRequest) message, 1000L));
        }
    }
}
