package com.divary.cimbtestrequirement.controller;

import com.divary.cimbtestrequirement.dto.response.BaseResponse;
import com.divary.cimbtestrequirement.service.UserService;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/user")
public class UserController extends BaseController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PreAuthorize(value = ROLE_USER_ADMIN)
    @GetMapping()
    @ApiOperation("Get Detail User")
    public ResponseEntity<BaseResponse<Object>> find(@RequestHeader(name = AUTHORIZATION, required = false) String jwt) {
        return getResponseOk(userService.findUser(jwt, null), "User Found");
    }

    @PreAuthorize(value = ROLE_ADMIN)
    @GetMapping("/{userId}")
    @ApiOperation("Get Detail Another User")
    public ResponseEntity<BaseResponse<Object>> find(@RequestHeader(name = AUTHORIZATION, required = false) String jwt, @PathVariable Long userId) {
        return getResponseOk(userService.findUser(jwt, userId), "User Found");
    }
}
