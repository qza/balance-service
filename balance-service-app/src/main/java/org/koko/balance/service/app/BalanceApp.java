package org.koko.balance.service.app;

import io.dropwizard.Application;
import io.dropwizard.assets.AssetsBundle;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import io.dropwizard.views.ViewBundle;

import org.koko.balance.service.app.data.BalanceLogPath;
import org.koko.balance.service.app.data.BalanceRepository;
import org.koko.balance.service.app.health.RepositoryHealthCheck;
import org.koko.balance.service.app.resources.BalanceCollectorResource;
import org.koko.balance.service.app.resources.BalanceExternalResource;
import org.koko.balance.service.app.resources.BalanceLogResource;
import org.koko.balance.service.app.resources.BalanceResource;
import org.koko.balance.service.app.tasks.GenerateBalanceLogTask;
import org.koko.balance.service.app.total.BalanceTotalResource;

/**
 * Balance application
 */
public class BalanceApp extends Application<BalanceAppConfig> {

    public static void main(String[] args) throws Exception {
        new BalanceApp().run(args);
    }

    @Override
    public void initialize(Bootstrap<BalanceAppConfig> bootstrap) {
        bootstrap.addBundle(new ViewBundle<>());
        bootstrap.addBundle(new AssetsBundle("/assets/css", "/css", null, "css"));
        bootstrap.addBundle(new AssetsBundle("/assets/js", "/js", null, "js"));
        bootstrap.addBundle(new AssetsBundle("/assets/fonts", "/fonts", null, "fonts"));
    }

    @Override
    public void run(BalanceAppConfig configuration, Environment environment) throws Exception {

        BalanceRepository balanceRepository = new BalanceRepository();

        BalanceResource resource = new BalanceResource(
                configuration.getBalanceMessageTemplate(), balanceRepository
        );
        environment.jersey().register(resource);

        RepositoryHealthCheck repositoryHealthCheck = new RepositoryHealthCheck(
                balanceRepository
        );
        environment.healthChecks().register("repository", repositoryHealthCheck);

        environment.jersey().register(new BalanceExternalResource());

        environment.jersey().register(new BalanceCollectorResource(configuration));

        BalanceLogPath logPath = new BalanceLogPath();

        environment.jersey().register(new BalanceLogResource(logPath));

        environment.admin().addTask(new GenerateBalanceLogTask(logPath));

        environment.jersey().register(new BalanceTotalResource(null, configuration));

    }

    @Override
    public String getName() {
        return "balance-service";
    }

}
