package com.Udemy.YeoGiDa.global.response.error;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class DefaultErrorResult<T> {
    private int code;
    private String message;
    private T data;

    public DefaultErrorResult(int code, String message) {
        this.code = code;
        this.message = message;
        this.data = null;
    }
}

