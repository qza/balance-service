package org.koko.balance.service.api;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.UUID;

/**
 * Balance event representation
 */
public class BalanceEvent {

    private String name;
    private UUID time;
    private Long amount;
    private Type type;

    public enum Type {
        PLACE, TAKE
    }

    public BalanceEvent() {
        // required for deserialization
    }

    public BalanceEvent(String name, UUID time, Long amount, Type type) {
        this.name = name;
        this.time = time;
        this.amount = amount;
        this.type = type;
    }

    @JsonProperty
    public String getName() {
        return name;
    }

    @JsonProperty
    public UUID getTime() {
        return time;
    }

    @JsonProperty
    public Long getAmount() {
        return amount;
    }

    @JsonProperty
    public Type getType() {
        return type;
    }

    @Override
    public int hashCode() {
        return this.time != null ? this.time.hashCode() : 0;
    }

    @Override
    public boolean equals(Object obj) {
        return obj != null && obj instanceof BalanceEvent && ((BalanceEvent) obj).getTime().equals(this.time);
    }

}
