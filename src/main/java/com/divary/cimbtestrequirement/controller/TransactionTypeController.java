package com.divary.cimbtestrequirement.controller;

import com.divary.cimbtestrequirement.dto.request.transactiontype.TransactionTypeReq;
import com.divary.cimbtestrequirement.dto.response.BaseResponse;
import com.divary.cimbtestrequirement.service.TransactionTypeService;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1/transaction-type")
public class TransactionTypeController extends BaseController {

    private final TransactionTypeService transactionTypeService;

    public TransactionTypeController(TransactionTypeService transactionTypeService) {
        this.transactionTypeService = transactionTypeService;
    }

    @PreAuthorize(value = ROLE_ADMIN)
    @PostMapping()
    @ApiOperation("Create Transaction Type")
    public ResponseEntity<BaseResponse<Object>> create(@RequestBody @Valid TransactionTypeReq form) {
        transactionTypeService.create(form);
        return getResponseCreated(null, "Register Success");
    }

    @GetMapping("/{id}")
    @ApiOperation("Find By Id Transaction Type")
    @PreAuthorize(value = ROLE_USER_ADMIN)
    public ResponseEntity<BaseResponse<Object>> findById(@PathVariable Long id) {
        return getResponseOk(transactionTypeService.findById(id), "Transaction Type Found");
    }

    @GetMapping()
    @PreAuthorize(value = ROLE_USER_ADMIN)
    @ApiOperation("Find All Transaction Type")
    public ResponseEntity<BaseResponse<Object>> findAll(@RequestHeader(name = AUTHORIZATION, required = false) String jwt, @RequestParam(defaultValue = "true") boolean active) {
        return getResponseList(transactionTypeService.findAll(jwt, active), "Transaction Type Found");
    }

    @PutMapping("/{id}")
    @PreAuthorize(value = ROLE_ADMIN)
    @ApiOperation("Update Transaction Type")
    public ResponseEntity<BaseResponse<Object>> update(@PathVariable Long id, @RequestBody @Valid TransactionTypeReq form) {
        return getResponseOk(transactionTypeService.update(id, form), "Update Success");
    }

    @PreAuthorize(value = ROLE_ADMIN)
    @DeleteMapping("/{id}")
    @ApiOperation("Delete Transaction Type")
    public ResponseEntity<BaseResponse<Object>> delete(@PathVariable Long id) {
        return getResponseOk(transactionTypeService.delete(id), "Update Success");
    }
}
