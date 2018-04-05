package com.db.awmd.challenge.web.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Data
public class AccountTransferReceiptDTO {

    @NotNull
    private String fromAccountId;

    @NotNull
    private String toAcountId;

    @NotNull
    private BigDecimal amount;

    @JsonCreator
    public AccountTransferReceiptDTO(@JsonProperty("from") String fromAccountId,
                                     @JsonProperty("to") String toAcountId,
                                     @JsonProperty("amount") BigDecimal amount) {
        this.fromAccountId = fromAccountId;
        this.toAcountId = toAcountId;
        this.amount = amount;
    }
}
