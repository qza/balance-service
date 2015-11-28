package org.koko.balance.service.app;

import io.dropwizard.Configuration;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.validator.constraints.NotEmpty;

import java.util.Map;

/**
 * Balance application configuration
 */
public class BalanceAppConfig extends Configuration {

    @NotEmpty
    private String balanceMessageTemplate;

    private Map<String, Map<String, String>> views;

    @JsonProperty
    public String getBalanceMessageTemplate() {
        return balanceMessageTemplate;
    }

    @JsonProperty
    public void setBalanceMessageTemplate(String balanceMessageTemplate) {
        this.balanceMessageTemplate = balanceMessageTemplate;
    }

    @JsonProperty
    public Map<String, Map<String, String>> getViews() {
        return views;
    }

    @JsonProperty
    public void setViews(Map<String, Map<String, String>> views) {
        this.views = views;
    }
}
