package com.divary.cimbtestrequirement.dto.request.auth;

import com.divary.cimbtestrequirement.constant.Messages;
import lombok.Getter;

import javax.validation.constraints.NotBlank;

@Getter
public class AuthReq {

    @NotBlank(message = "username " + Messages.IS_REQUIRED)
    private String username;

    @NotBlank(message = "password " + Messages.IS_REQUIRED)
    private String password;
}
