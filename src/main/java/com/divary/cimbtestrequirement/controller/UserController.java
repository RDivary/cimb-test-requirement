package com.divary.cimbtestrequirement.controller;

import com.divary.cimbtestrequirement.dto.response.BaseResponse;
import com.divary.cimbtestrequirement.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/user")
public class UserController extends BaseController{

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PreAuthorize(value = ROLE_USER_ADMIN)
    @GetMapping()
    public ResponseEntity<BaseResponse<Object>> find(@RequestHeader(AUTHORIZATION) String jwt) {
        return getResponseOk(userService.findUser(jwt, null), "User Found");
    }

    @PreAuthorize(value = ROLE_ADMIN)
    @GetMapping("/{userId}")
    public ResponseEntity<BaseResponse<Object>> find(@RequestHeader(AUTHORIZATION) String jwt, @PathVariable Long userId) {
        return getResponseOk(userService.findUser(jwt, userId), "User Found");
    }
}
