package org.koko.balance.service.app.data;

import java.util.Random;

import static javax.ws.rs.core.Response.*;

/**
 * Random stuff
 */
public class Randomised {

    public static Status httpOkNotAvailable() {
        return new Status[]{
                Status.OK,
                Status.SERVICE_UNAVAILABLE,
                Status.SERVICE_UNAVAILABLE,
                Status.INTERNAL_SERVER_ERROR}[
                new Random().nextInt(4)];
    }

    public static Status httpClientServer() {
        return new Status[]{
                Status.BAD_REQUEST,
                Status.INTERNAL_SERVER_ERROR,
                Status.NOT_ACCEPTABLE,
                Status.SERVICE_UNAVAILABLE}[
                new Random().nextInt(4)];
    }

}
