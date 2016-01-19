package org.koko.balance.service.app.resources;

import io.dropwizard.testing.ResourceHelpers;
import io.dropwizard.testing.junit.DropwizardAppRule;

import okhttp3.OkHttpClient;

import org.junit.ClassRule;

import org.koko.balance.service.app.BalanceApp;
import org.koko.balance.service.app.BalanceAppConfig;

import org.junit.Test;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.*;

/**
 * Balance collector resource test
 */
public class BalanceCollectorResourceTest {

    OkHttpClient client = new OkHttpClient().newBuilder().readTimeout(1, TimeUnit.MINUTES).build();

    @ClassRule
    public static final DropwizardAppRule<BalanceAppConfig> RULE =
            new DropwizardAppRule<>(BalanceApp.class, ResourceHelpers.resourceFilePath("app-test.yml"));

    @Test
    public void loginHandlerRedirectsAfterPost() throws IOException {

        String url = "http://localhost:" + RULE.getLocalPort() + "/balances/total/mark";

        okhttp3.Request request = new okhttp3.Request.Builder().url(url).build();

        okhttp3.Response response = client.newCall(request).execute();

        assertTrue(response.isSuccessful());
    }

}
