package org.koko.balance.service.app;

import io.dropwizard.Configuration;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.dropwizard.metrics.graphite.GraphiteReporterFactory;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;
import java.util.Collections;
import java.util.Map;

/**
 * Balance application configuration
 */
public class BalanceAppConfig extends Configuration {

    @NotEmpty
    private String balanceMessageTemplate;

    @NotEmpty
    private String bankUrlTemplate;

    @NotNull
    private Map<String, Map<String, String>> views = Collections.emptyMap();

    @NotNull
    private GraphiteReporterFactory metrics = new GraphiteReporterFactory();

    @JsonProperty
    public String getBalanceMessageTemplate() {
        return balanceMessageTemplate;
    }

    @JsonProperty
    public void setBalanceMessageTemplate(String balanceMessageTemplate) {
        this.balanceMessageTemplate = balanceMessageTemplate;
    }

    @JsonProperty
    public String getBankUrlTemplate() {
        return bankUrlTemplate;
    }

    @JsonProperty
    public void setBankUrlTemplate(String bankUrlTemplate) {
        this.bankUrlTemplate = bankUrlTemplate;
    }

    @JsonProperty
    public Map<String, Map<String, String>> getViews() {
        return views;
    }

    @JsonProperty
    public void setViews(Map<String, Map<String, String>> views) {
        this.views = views;
    }

    @JsonProperty
    public GraphiteReporterFactory getMetrics() {
        return metrics;
    }

    @JsonProperty
    public void setMetrics(GraphiteReporterFactory metrics) {
        this.metrics = metrics;
    }
}
