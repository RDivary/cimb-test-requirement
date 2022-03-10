package com.divary.cimbtestrequirement.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class BaseResponse<T> {
    private T data;

    private int code;

    private String status;

    private String message;
}
