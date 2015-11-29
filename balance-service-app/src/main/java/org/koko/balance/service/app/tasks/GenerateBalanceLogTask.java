package org.koko.balance.service.app.tasks;

import org.koko.balance.service.api.BalanceEvent;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.ImmutableMultimap;

import io.dropwizard.jackson.Jackson;
import io.dropwizard.servlets.tasks.Task;

import org.koko.balance.service.app.data.BalanceLogPath;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.text.DecimalFormat;
import java.util.Random;
import java.util.UUID;

/**
 * Generates balance log for streaming demo
 */
public class GenerateBalanceLogTask extends Task {

    private static final Logger log = LoggerFactory.getLogger(GenerateBalanceLogTask.class);

    private final BalanceLogPath balanceLogPath;

    public GenerateBalanceLogTask(BalanceLogPath balanceLogPath) {
        super("generate");
        this.balanceLogPath = balanceLogPath;
    }

    @Override
    public void execute(ImmutableMultimap<String, String> parameters, PrintWriter output) throws Exception {

        long counter = 0;
        long maxCount = 100000L;
        long maxSize = 1024 * 1024 * 1024L; // 1Gb
        long start = System.currentTimeMillis();

        if(parameters.containsKey("count")) {
            maxCount = Long.parseLong(parameters.get("count").iterator().next());
        }

        if(parameters.containsKey("size")) {
            maxSize = Long.parseLong(parameters.get("size").iterator().next());
        }

        log.info("generate balance_log task started");

        File file = balanceLogPath.getPath().toFile();
        ObjectMapper objectMapper = Jackson.newObjectMapper();
        FileOutputStream fileOutputStream = new FileOutputStream(file);
        OutputStreamWriter outputStreamWriter = new OutputStreamWriter(fileOutputStream, "UTF-8");
        BufferedWriter bufferedWriter = new BufferedWriter(outputStreamWriter);
        PrintWriter writer = new PrintWriter(bufferedWriter, false);

        Random random = new Random();
        String[] names = new String[]{"tom", "mark", "john"};

        while (file.length() < maxSize && counter < maxCount) {

            counter += 1;
            writer.println(
                    objectMapper.writeValueAsString(
                            new BalanceEvent(
                                    names[random.nextInt(3)],
                                    UUID.randomUUID(),
                                    random.nextLong() / 1000,
                                    BalanceEvent.Type.values()[random.nextInt(2)])));

            if (counter >= 1000 && counter % 1000 == 0) {
                output.println("generating record " + counter);
                output.flush();
            }
        }

        writer.close();

        long time = System.currentTimeMillis() - start;
        String duration = new DecimalFormat().format(Long.valueOf(time / 1000L));
        String sizeGb = new DecimalFormat().format(file.length() / maxSize);

        output.append(String.format("generated balance_log in %s seconds with size: %s GB", duration, sizeGb));
    }

}
