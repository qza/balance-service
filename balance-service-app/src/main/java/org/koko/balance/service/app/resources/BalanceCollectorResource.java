package org.koko.balance.service.app.resources;

import com.codahale.metrics.annotation.Timed;

import org.glassfish.jersey.client.rx.Rx;
import org.glassfish.jersey.client.rx.RxClient;
import org.glassfish.jersey.client.rx.rxjava.RxObservable;
import org.glassfish.jersey.client.rx.rxjava.RxObservableInvoker;

import org.koko.balance.service.api.BalanceResponse;
import org.koko.balance.service.app.BalanceAppConfig;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import rx.Observable;
import rx.schedulers.Schedulers;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import javax.ws.rs.core.MediaType;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Balance collector resource that aggregates balances from other banks
 */
@Path("/balances/total/{name}")
public class BalanceCollectorResource {

    private static final Logger log = LoggerFactory.getLogger(BalanceCollectorResource.class);

    private final RxClient<RxObservableInvoker> rxClient = Rx.newClient(RxObservableInvoker.class);

    private final AtomicLong requestCounter = new AtomicLong();

    private final BalanceAppConfig appConfig;

    public BalanceCollectorResource(BalanceAppConfig appConfig) {
        this.appConfig = appConfig;
    }

    @GET
    @Timed
    @Produces(MediaType.APPLICATION_JSON)
    public BalanceResponse balanceTotal(@PathParam("name") String name) {

        log.info("total balance get request [name:{}]", name);

        CountDownLatch latch = new CountDownLatch(1);

        Observable<BalanceResponse> bank1 = RxObservable.from(rxClient
                .target(appConfig.getBank1BalancePath()))
                .resolveTemplate("name", name)
                .request()
                .rx()
                .get(BalanceResponse.class)
                .onErrorReturn(throwable -> {
                    log.warn("bank1 error: " + throwable.getMessage());
                    return new BalanceResponse();
                });

        Observable<BalanceResponse> bank2 = RxObservable.from(rxClient
                .target(appConfig.getBank2BalancePath()))
                .resolveTemplate("name", name)
                .request()
                .rx()
                .get(BalanceResponse.class)
                .onErrorReturn(throwable -> {
                    log.warn("bank2 error: " + throwable.getMessage());
                    return new BalanceResponse();
                });

        BalanceResponse balanceResponse = new BalanceResponse(requestCounter.incrementAndGet(), name, 0L, "Total on all accounts");

        Observable.just(balanceResponse)
                .zipWith(bank1, (response, bank1Response) -> {
                    response.addBalance(bank1Response.getBalance());
                    return response;
                })
                .zipWith(bank2, (response, bank2Response) -> {
                    response.addBalance(bank2Response.getBalance());
                    return response;
                })
                .observeOn(Schedulers.io())
                .subscribe(
                        response -> log.info("balance collected"),
                        throwable -> log.error("error collecting balance", throwable),
                        () -> latch.countDown()
                );

        try {
            latch.await();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        return balanceResponse;
    }

}
