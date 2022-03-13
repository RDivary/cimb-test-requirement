package com.divary.cimbtestrequirement.controller;

import com.divary.cimbtestrequirement.dto.request.transaction.ExecuteReq;
import com.divary.cimbtestrequirement.dto.response.BaseResponse;
import com.divary.cimbtestrequirement.service.TransactionService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("api/v1/transaction")
public class TransactionController extends BaseController {

    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @PreAuthorize(value = ROLE_USER)
    @PostMapping()
    public ResponseEntity<BaseResponse<Object>> create(@RequestHeader(AUTHORIZATION) String jwt, @RequestBody @Valid ExecuteReq form) {
        return getResponseCreated(transactionService.execute(jwt, form), "Register Success");
    }

    @PreAuthorize(value = ROLE_USER_ADMIN)
    @GetMapping("/{userId}")
    public ResponseEntity<BaseResponse<Object>> findById(@RequestHeader(AUTHORIZATION) String jwt, @PathVariable Long userId) {
        return getResponseOk(transactionService.findById(jwt, userId), "Transaction Found");
    }

    @PreAuthorize(value = ROLE_ADMIN)
    @GetMapping("/user/{userId}")
    public ResponseEntity<BaseResponse<Object>> findAll(@RequestHeader(AUTHORIZATION) String jwt, @PathVariable() Long userId, @RequestParam(defaultValue = "") String transactionName) {
        return getResponseList(transactionService.findAll(jwt, userId, transactionName), "Transaction");
    }

    @PreAuthorize(value = ROLE_USER_ADMIN)
    @GetMapping("/user")
    public ResponseEntity<BaseResponse<Object>> findAll(@RequestHeader(AUTHORIZATION) String jwt, @RequestParam(defaultValue = "") String transactionName) {
        return getResponseList(transactionService.findAll(jwt, null, transactionName), "Transaction");
    }
}
