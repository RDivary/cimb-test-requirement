package com.divary.cimbtestrequirement.dto.response.auth;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class LoginResp {
    private final String type = "Bearer";
    private String token;
    private String username;
}