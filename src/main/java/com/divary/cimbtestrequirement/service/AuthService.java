package com.divary.cimbtestrequirement.service;

import com.divary.cimbtestrequirement.dto.request.auth.AuthReq;
import com.divary.cimbtestrequirement.dto.response.auth.LoginResp;
import com.divary.cimbtestrequirement.enums.RolesEnum;

import java.security.NoSuchAlgorithmException;

public interface AuthService {
    void register(AuthReq form, RolesEnum role) throws NoSuchAlgorithmException;

    LoginResp login(AuthReq form);
}
