package com.divary.cimbtestrequirement.dto.request.transactiontype;

import com.divary.cimbtestrequirement.constant.Messages;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TransactionTypeReq {
    @NotBlank(message = "transaction code " + Messages.IS_REQUIRED)
    private String transactionCode;
    @NotBlank(message = "transaction name " + Messages.IS_REQUIRED)
    private String transactionName;
}
