package com.divary.cimbtestrequirement.dto.request.transaction;

import com.divary.cimbtestrequirement.constant.Messages;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ExecuteReq {
    @NotNull(message = "amount " + Messages.IS_REQUIRED)
    private Long amount;
    @NotNull(message = "transaction type id " + Messages.IS_REQUIRED)
    private Long transactionTypeId;
}
