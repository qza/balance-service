package org.koko.balance.service.app.tasks;

import com.google.common.collect.ImmutableMultimap;
import org.junit.Test;
import org.koko.balance.service.app.data.BalanceLogPath;

import java.io.PrintWriter;

import static org.mockito.Mockito.*;

/**
 * Generate balance log task test
 */
public class GenerateBalanceLogTaskTest {

    BalanceLogPath path = new BalanceLogPath("src/test/resources/balance_log");
    GenerateBalanceLogTask task = new GenerateBalanceLogTask(path);

    @Test
    public void shouldGenerateTestBalanceLogWithCount() throws Exception {
        PrintWriter output = mock(PrintWriter.class);
        ImmutableMultimap<String, String> parameters = ImmutableMultimap.of("count","2000");
        task.execute(parameters, output);
        verify(output).append(any(CharSequence.class));
        reset(output);
    }

    @Test
    public void shouldGenerateTestBalanceLogWithSize() throws Exception {
        PrintWriter output = mock(PrintWriter.class);
        ImmutableMultimap<String, String> parameters = ImmutableMultimap.of("size","1000");
        task.execute(parameters, output);
        verify(output).append(any(CharSequence.class));
        reset(output);
    }

}
