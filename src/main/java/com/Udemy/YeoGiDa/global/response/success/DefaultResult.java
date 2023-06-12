package com.Udemy.YeoGiDa.global.response.success;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class DefaultResult<T> {

    private int code;
    private String message;
    private T data;

    public DefaultResult(final int code, final String message) {
        this.code = code;
        this.message = message;
        this.data = null;
    }

    public static<T> DefaultResult<T> res(final int statusCode, final String responseMessage) {
        return res(statusCode, responseMessage, null);
    }

    public static<T> DefaultResult<T> res(final int statusCode, final String responseMessage, final T t) {
        return DefaultResult.<T>builder()
                .code(statusCode)
                .message(responseMessage)
                .data(t)
                .build();
    }
}
