package com.divary.cimbtestrequirement.dto.request.auth;

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
public class AuthReq {

    @NotBlank(message = "username " + Messages.IS_REQUIRED)
    private String username;

    @NotBlank(message = "password " + Messages.IS_REQUIRED)
    private String password;
}
