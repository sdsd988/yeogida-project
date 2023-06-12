package com.Udemy.YeoGiDa.global.jwt.exception;

public class TokenIsInvalidException extends Exception {
    public TokenIsInvalidException() {
        super("Token is invalid");
    }
}