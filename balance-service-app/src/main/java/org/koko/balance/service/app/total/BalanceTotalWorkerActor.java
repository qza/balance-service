package org.koko.balance.service.app.total;

import akka.actor.ActorRef;
import akka.actor.UntypedActor;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.dropwizard.jackson.Jackson;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import org.koko.balance.service.api.BalanceResponse;
import org.koko.balance.service.app.BalanceAppConfig;

import java.io.IOException;
import java.util.Optional;

/**
 * Balance total worker fetching balance from external http resources
 */
public class BalanceTotalWorkerActor extends UntypedActor {

    private final ActorRef master;

    private final OkHttpClient client;

    private final BalanceAppConfig appConfig;

    private final ObjectMapper mapper = Jackson.newObjectMapper();

    public BalanceTotalWorkerActor(OkHttpClient client, BalanceAppConfig appConfig, ActorRef master) {
        this.client = client;
        this.appConfig = appConfig;
        this.master = master;
    }

    @Override
    public void onReceive(Object message) throws Exception {

        if (message instanceof BalanceTotalRequest) {

            BalanceTotalRequest balanceTotalRequest = (BalanceTotalRequest) message;

            Optional.ofNullable(balanceTotalRequest.getBank()).orElseThrow(() ->
                    new IllegalArgumentException("external resources should be defined")
            );

            String url = appConfig.getBankUrlTemplate()
                    .replaceFirst("\\{bank\\}", balanceTotalRequest.getBank())
                    .replaceFirst("\\{name\\}", balanceTotalRequest.getName());

            Request request = new Request.Builder().url(url).build();

            Response response = client.newCall(request).execute();

            try {

                if (!response.isSuccessful()) {
                    response.body().close();
                    throw new BalanceTotalWorkerActorException("call failed" +
                            " [bank:" + balanceTotalRequest.getBank() + "]" +
                            " [name:" + balanceTotalRequest.getName() + "]",
                            balanceTotalRequest);
                }

                String responseBody = response.body().string();
                BalanceResponse balanceResponse = mapper.readValue(responseBody, BalanceResponse.class);
                getSender().tell(new BalanceTotalResponse((BalanceTotalRequest) message, balanceResponse.getBalance()));
                response.body().close();

            } catch (IOException ioex) {
                throw new BalanceTotalWorkerActorException("other http error", ioex);
            }

        }
    }

    @Override
    public void postRestart(Throwable reason) {
        master.tell(reason, getSelf());
    }
}