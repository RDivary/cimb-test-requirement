package com.divary.cimbtestrequirement.controller;

import com.divary.cimbtestrequirement.dto.request.auth.AuthReq;
import com.divary.cimbtestrequirement.dto.response.BaseResponse;
import com.divary.cimbtestrequirement.enums.RolesEnum;
import com.divary.cimbtestrequirement.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.security.NoSuchAlgorithmException;

@RestController
@RequestMapping("api/v1/auth")
public class AuthController extends BaseController {

    private AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<BaseResponse<Object>> register(@RequestBody @Valid AuthReq form) throws NoSuchAlgorithmException {
        authService.register(form, RolesEnum.ROLE_USER);
        return getResponseCreated(null, "Register Success");
    }

    @PostMapping("/login")
    public ResponseEntity<BaseResponse<Object>> login(@RequestBody @Valid AuthReq form) {
        return getResponseOk(authService.login(form), "Login Success");
    }

}
