package org.koko.balance.service.app.resources;

import com.codahale.metrics.annotation.Timed;

import org.koko.balance.service.app.data.BalanceLogPath;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import rx.Observable;
import rx.Observer;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.StreamingOutput;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;

import java.util.concurrent.CountDownLatch;
import java.util.stream.Stream;

/**
 * Balance event resource
 */
@Path("/balances/log")
public class BalanceLogResource {

    private static final Logger log = LoggerFactory.getLogger(BalanceLogResource.class);

    private final BalanceLogPath balanceLogPath;

    public BalanceLogResource(BalanceLogPath balanceLogPath) {
        this.balanceLogPath = balanceLogPath;
    }

    @GET
    @Timed
    @Produces(MediaType.APPLICATION_JSON)
    public Response stream() {
        try {
            Stream<String> fileStream = Files.lines(balanceLogPath.getPath());

            Observable<String> fileObservable = Observable.from(fileStream::iterator);

            FileStreamingOutput streamingOutput = new FileStreamingOutput(fileObservable);

            return Response.ok().entity(streamingOutput).build();

        } catch (IOException e) {
            log.error("error while opening log file stream", e);
            throw new WebApplicationException("log file error");
        }
    }

    private class FileStreamingOutput implements StreamingOutput {

        private Observable<String> fileObservable;

        FileStreamingOutput(Observable<String> fileObservable) {
            this.fileObservable = fileObservable;
        }

        @Override
        public void write(OutputStream outputStream) throws IOException, WebApplicationException {
            CountDownLatch latch = new CountDownLatch(1);
            FileStreamingObserver fileStreamingObserver = new FileStreamingObserver(outputStream, latch);
            fileObservable.subscribe(fileStreamingObserver);
            try {
                latch.await();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    private class FileStreamingObserver implements Observer<String> {

        final OutputStream output;
        final CountDownLatch latch;

        FileStreamingObserver(OutputStream output, CountDownLatch latch) {
            this.output = output;
            this.latch = latch;
        }

        @Override
        public void onNext(String row) {
            try {
                output.write(row.getBytes());
            } catch (IOException e) {
                log.warn("error during write", e);
            }
        }

        @Override
        public void onError(Throwable throwable) {
            log.error("stream error");
        }

        @Override
        public void onCompleted() {
            try {
                log.info("stream end");
                output.close();
            } catch (IOException e) {
                log.warn("error closing output", e);
            } finally {
                latch.countDown();
            }
        }
    }

}