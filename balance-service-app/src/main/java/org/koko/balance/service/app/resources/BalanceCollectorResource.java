package org.koko.balance.service.app.resources;

import com.codahale.metrics.annotation.Timed;

import org.glassfish.jersey.client.rx.Rx;
import org.glassfish.jersey.client.rx.RxClient;
import org.glassfish.jersey.client.rx.RxWebTarget;
import org.glassfish.jersey.client.rx.rxjava.RxObservable;
import org.glassfish.jersey.client.rx.rxjava.RxObservableInvoker;

import org.koko.balance.service.api.BalanceResponse;
import org.koko.balance.service.app.BalanceAppConfig;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import rx.Observable;

import javax.ws.rs.*;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Balance collector resource for aggregating balances on other accounts
 */
@Path("/balances/total/{name}")
public class BalanceCollectorResource {

    private static final Logger log = LoggerFactory.getLogger(BalanceCollectorResource.class);

    private final AtomicLong requestCounter = new AtomicLong();

    private final BalanceAppConfig appConfig;

    public BalanceCollectorResource(BalanceAppConfig appConfig) {
        this.appConfig = appConfig;
    }

    @GET
    @Timed
    @Produces(MediaType.APPLICATION_JSON)
    public Response balanceTotal(@PathParam("name") String name) {

        log.debug("total balance get request [name:{}]", name);

        BalanceResponse response = collectBalances(name);

        log.debug("total balance request served [name:{}]", name);

        return Response.ok().entity(response).build();
    }

    private BalanceResponse collectBalances(String name) {

        BalanceResponse balanceResponse = new BalanceResponse(
                requestCounter.incrementAndGet(), name, 0L, "total on all accounts");

        RxClient<RxObservableInvoker> rxClient = Rx.newClient(RxObservableInvoker.class);

        RxWebTarget<RxObservableInvoker> rxWebTarget = RxObservable.from(
                rxClient.target(appConfig.getBankUrlTemplate())
        );

        List<Observable<BalanceResponse>> responses = new ArrayList<>(3);

        Arrays.asList("bank1", "bank2", "bank3").forEach(
                (bank) -> responses.add(rxWebTarget
                        .resolveTemplate("bank", bank)
                        .resolveTemplate("name", name)
                        .request()
                        .rx()
                        .get(BalanceResponse.class)
                        .timeout(5, TimeUnit.SECONDS)
                        .retryWhen(attempts -> attempts
                                .zipWith(Observable.range(1, 5), (throwable, i) -> new ThrowableCount(throwable, i))
                                .flatMap(tc -> {
                                    boolean shouldRetry = tc.getCount() < 5 && tc.isServerError();
                                    log.warn("{} error :: {} :: {}", tc.getThrowable().getMessage(), bank,
                                            shouldRetry ? "retry in " + tc.getCount() + " second(s)" : "call failed");
                                    return shouldRetry ? Observable.timer(tc.getCount(), TimeUnit.SECONDS)
                                            : Observable.error(tc.getThrowable());
                                })
                        )
                        .onErrorReturn(throwable -> new BalanceResponse())
                        .finallyDo(() -> log.info("{} call completed", bank))
                )
        );

        balanceResponse =
                Observable.merge(responses)
                        .reduce(balanceResponse, (sum, el) -> sum.addBalance(el.getBalance()))
                        .toBlocking().first();

        log.info("balance collected: " + balanceResponse.getBalance());

        return balanceResponse;
    }

    /**
     * Holding error and count
     */
    public static class ThrowableCount {

        private final Throwable throwable;
        private final int count;

        public ThrowableCount(Throwable throwable, int count) {
            this.throwable = throwable;
            this.count = count;
        }

        public Throwable getThrowable() {
            return throwable;
        }

        public int getCount() {
            return count;
        }

        public boolean isServerError() {
            return throwable != null && throwable.getCause() != null &&
                    (throwable.getCause() instanceof InternalServerErrorException ||
                            throwable.getCause() instanceof ServiceUnavailableException);
        }
    }


}
