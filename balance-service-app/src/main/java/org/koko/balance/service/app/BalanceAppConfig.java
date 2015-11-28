package org.koko.balance.service.app;

import io.dropwizard.Configuration;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.validator.constraints.NotEmpty;

/**
 * Balance application configuration
 */
public class BalanceAppConfig extends Configuration {

    @NotEmpty
    private String balanceMessageTemplate;

    @JsonProperty
    public String getBalanceMessageTemplate() {
        return balanceMessageTemplate;
    }

    @JsonProperty
    public void setBalanceMessageTemplate(String balanceMessageTemplate) {
        this.balanceMessageTemplate = balanceMessageTemplate;
    }
}
