package org.koko.balance.service.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.validator.constraints.Length;

import java.text.NumberFormat;
import java.util.Locale;

/**
 * Balance response representation
 */
public class BalanceResponse {

    private Long requestId;

    private String name;

    private Long balance = 0L;

    @Length(max = 140)
    private String message;

    public BalanceResponse() {
        // required to deserialize
    }

    public BalanceResponse(Long requestId, String name, Long balance, String message) {
        this.requestId = requestId;
        this.name = name;
        this.balance = balance;
        this.message = message;
    }

    public BalanceResponse addBalance(Long balance) {
        return new BalanceResponse(this.requestId, this.name, this.balance + balance, this.message);
    }

    @JsonProperty
    public Long getRequestId() {
        return requestId;
    }

    @JsonProperty
    public String getMessage() {
        return message;
    }

    @JsonProperty
    public Long getBalance() {
        return balance;
    }

    @JsonProperty
    public String getName() {
        return name;
    }

    @Override
    public int hashCode() {
        return this.requestId != null ? this.requestId.hashCode() : 0;
    }

    @Override
    public boolean equals(Object obj) {
        return obj != null && obj instanceof BalanceResponse && ((BalanceResponse) obj).getRequestId().equals(this.requestId);
    }

    @Override
    public String toString() {
        return "name: " + name + ", balance: " + NumberFormat.getCurrencyInstance(Locale.US).format(balance);
    }
}
