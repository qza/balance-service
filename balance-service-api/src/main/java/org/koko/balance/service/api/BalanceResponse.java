package org.koko.balance.service.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.validator.constraints.Length;

/**
 * Balance response representation
 */
public class BalanceResponse {

    private Long requestId;

    private String name;

    private Long balance;

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
}
