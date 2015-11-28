package org.koko.balance.service.app.views;

import io.dropwizard.views.View;
import org.koko.balance.service.api.BalanceResponse;

/**
 * Balance view with freemarker template
 */
public class BalanceView extends View {

    final BalanceResponse model;

    public BalanceView(BalanceResponse model) {
        super("/views/balance.ftl");
        this.model = model;
    }

    public BalanceResponse getModel() {
        return model;
    }
}
