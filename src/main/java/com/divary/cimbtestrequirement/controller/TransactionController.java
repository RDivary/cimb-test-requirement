package com.divary.cimbtestrequirement.controller;

import com.divary.cimbtestrequirement.dto.request.transaction.ExecuteReq;
import com.divary.cimbtestrequirement.dto.response.BaseResponse;
import com.divary.cimbtestrequirement.service.TransactionService;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;

@RestController
@RequestMapping("api/v1/transaction")
public class TransactionController extends BaseController {

    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @PreAuthorize(value = ROLE_USER)
    @PostMapping()
    @ApiOperation("Execute Transaction")
    public ResponseEntity<BaseResponse<Object>> create(@RequestHeader(name = AUTHORIZATION, required = false) String jwt, @RequestBody @Valid ExecuteReq form) {
        return getResponseCreated(transactionService.execute(jwt, form), "Register Success");
    }

    @PreAuthorize(value = ROLE_USER_ADMIN)
    @GetMapping("/{userId}")
    @ApiOperation("Find Transaction By Id")
    public ResponseEntity<BaseResponse<Object>> findById(@RequestHeader(name = AUTHORIZATION, required = false) String jwt, @PathVariable Long userId) {
        return getResponseOk(transactionService.findById(jwt, userId), "Transaction Found");
    }

    @PreAuthorize(value = ROLE_ADMIN)
    @GetMapping("/user/{userId}")
    @ApiOperation("Find All Another User Transaction")
    public ResponseEntity<BaseResponse<Object>> findAll(@RequestHeader(name = AUTHORIZATION, required = false) String jwt, @PathVariable() Long userId, @RequestParam(defaultValue = "") String transactionName) {
        return getResponseList(transactionService.findAll(jwt, userId, transactionName), "Transaction");
    }

    @PreAuthorize(value = ROLE_USER_ADMIN)
    @GetMapping("/user")
    @ApiOperation("Find All Transaction")
    public ResponseEntity<BaseResponse<Object>> findAll(@RequestHeader(name = AUTHORIZATION, required = false) String jwt, @RequestParam(defaultValue = "") String transactionName) {
        return getResponseList(transactionService.findAll(jwt, null, transactionName), "Transaction");
    }

    @PreAuthorize(value = ROLE_USER)
    @GetMapping(value = "/download-report")
    @ApiOperation("Download Transaction xlsx format")
    public ResponseEntity<Object> downloadReport(@RequestHeader(name = AUTHORIZATION, required = false) String jwt) {
        HashMap<String, Object> result = transactionService.generateTransactionHistory(jwt, null);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + result.getOrDefault("fileName", "Report Transaction.xlsx"))
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .body(result.get("file"));
    }

    @PreAuthorize(value = ROLE_ADMIN)
    @GetMapping(value = "/download-report/{userId}")
    @ApiOperation("Download Transaction Another User xlsx format")
    public ResponseEntity<Object> downloadReport(@RequestHeader(name = AUTHORIZATION, required = false) String jwt, @PathVariable Long userId) {
        HashMap<String, Object> result = transactionService.generateTransactionHistory(jwt, userId);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + result.getOrDefault("fileName", "Report Transaction.xlsx"))
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .body(result.get("file"));
    }
}
