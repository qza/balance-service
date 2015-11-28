package org.koko.balance.service.app.views;

import org.junit.Test;
import org.koko.balance.service.api.BalanceResponse;

import static org.junit.Assert.assertNotNull;

/**
 * Balance view test
 */
public class BalanceViewTest {

    @Test
    public void shouldMakeBalanceView() {
        BalanceView view = new BalanceView(new BalanceResponse());
        assertNotNull(view.getTemplateName());
        assertNotNull(view.getModel());
    }

}
