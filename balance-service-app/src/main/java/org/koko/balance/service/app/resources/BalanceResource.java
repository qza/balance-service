package org.koko.balance.service.app.resources;

import com.codahale.metrics.annotation.Timed;

import org.hibernate.validator.constraints.NotEmpty;
import org.koko.balance.service.api.BalanceResponse;
import org.koko.balance.service.app.data.BalanceRepository;

import org.koko.balance.service.app.views.BalanceView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.*;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;

import java.net.URI;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Locale;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Balance resource
 */
@Path("/balances/{name}")
public class BalanceResource {

    private static final Logger log = LoggerFactory.getLogger(BalanceResource.class);

    private final String messageTemplate;

    private final BalanceRepository repository;

    private final AtomicLong requestIdGen;

    public BalanceResource(String messageTemplate, BalanceRepository repository) {
        this.messageTemplate = messageTemplate;
        this.repository = repository;
        this.requestIdGen = new AtomicLong();
    }

    @GET
    @Timed
    @Produces(MediaType.APPLICATION_JSON)
    public BalanceResponse get(@PathParam("name") @NotEmpty String name) {

        Long requestId = requestIdGen.incrementAndGet();

        log.info("get request [id:{}] [name:{}] [balance:{}]", requestId, name);

        Long balance = Optional.ofNullable(repository.get(name)).orElseThrow(() ->
                new NotFoundException("not found [name: " + name + "]")
        );

        NumberFormat nf = NumberFormat.getCurrencyInstance(Locale.US);
        String message = String.format(messageTemplate, nf.format(balance.doubleValue()));

        return new BalanceResponse(requestId, name, balance, message);
    }

    @GET
    @Timed
    @Produces(MediaType.TEXT_HTML)
    public BalanceView view(@PathParam("name") @NotEmpty String name) {
        return new BalanceView(get(name));
    }

    @PUT
    @Timed
    @Produces(MediaType.APPLICATION_JSON)
    public Response put(@PathParam("name") @NotEmpty String name, @QueryParam("balance") @NotEmpty String balance) {

        Long balanceVal;
        Long requestId = requestIdGen.incrementAndGet();

        log.info("put request [id:{}] [name:{}] [balance:{}]", requestId, name, balance);

        try {
            balanceVal = NumberFormat.getCurrencyInstance(Locale.US).parse(balance).longValue();

        } catch (ParseException e) {
            log.info("unable to parse number [request:{}][value:{}]", requestId, balance);
            throw new BadRequestException("unexpected balance format [example: 70,111,222.00]");
        }

        URI resource = UriBuilder.fromResource(BalanceResource.class).build(name);

        boolean created = repository.put(name, balanceVal);

        return created ? Response.created(resource).build() : Response.ok(resource).build();
    }

}
