package org.koko.balance.service.app.total;

import io.dropwizard.testing.ResourceHelpers;
import io.dropwizard.testing.junit.DropwizardAppRule;

import okhttp3.OkHttpClient;

import org.junit.ClassRule;
import org.junit.Test;

import org.koko.balance.service.app.BalanceApp;
import org.koko.balance.service.app.BalanceAppConfig;

import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertTrue;

/**
 * BalanceTotalResource test class
 */
public class BalanceTotalResourceTest {

    OkHttpClient client = new OkHttpClient().newBuilder().readTimeout(1, TimeUnit.MINUTES).build();

    @ClassRule
    public static final DropwizardAppRule<BalanceAppConfig> RULE =
            new DropwizardAppRule<>(BalanceApp.class, ResourceHelpers.resourceFilePath("app-test.yml"));

    @Test
    public void shouldCalculateTotal() throws Exception {

        String url = "http://localhost:" + RULE.getLocalPort() + "/balances/total/akka/mark";

        okhttp3.Request request = new okhttp3.Request.Builder().url(url).build();

        okhttp3.Response response = client.newCall(request).execute();

        assertTrue(response.isSuccessful());
    }

}