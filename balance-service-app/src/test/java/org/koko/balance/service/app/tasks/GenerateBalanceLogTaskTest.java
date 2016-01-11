package org.koko.balance.service.app.tasks;

import com.google.common.collect.ImmutableMultimap;
import org.junit.Test;
import org.koko.balance.service.app.data.BalanceLogPath;

import java.io.PrintWriter;
import java.nio.file.Files;

import static org.mockito.Mockito.*;

/**
 * Generate balance log task test
 */
public class GenerateBalanceLogTaskTest {

    @Test
    public void shouldGenerateTestBalanceLogWithCount() throws Exception {
        BalanceLogPath path = deleteCreate(new BalanceLogPath("test_balance_log_1"));
        GenerateBalanceLogTask task = new GenerateBalanceLogTask(path);
        PrintWriter output = mock(PrintWriter.class);
        ImmutableMultimap<String, String> parameters = ImmutableMultimap.of("count","2000");
        task.execute(parameters, output);
        verify(output).append(any(CharSequence.class));
        reset(output);
    }

    @Test
    public void shouldGenerateTestBalanceLogWithSize() throws Exception {
        BalanceLogPath path = deleteCreate(new BalanceLogPath("test_balance_log_2"));
        GenerateBalanceLogTask task = new GenerateBalanceLogTask(path);
        PrintWriter output = mock(PrintWriter.class);
        ImmutableMultimap<String, String> parameters = ImmutableMultimap.of("size","1000");
        task.execute(parameters, output);
        verify(output).append(any(CharSequence.class));
        reset(output);
    }

    public BalanceLogPath deleteCreate(BalanceLogPath path) throws Exception {
        if(Files.exists(path.getPath())) {
            Files.delete(path.getPath());
        }
        Files.createFile(path.getPath());
        return path;
    }

}
