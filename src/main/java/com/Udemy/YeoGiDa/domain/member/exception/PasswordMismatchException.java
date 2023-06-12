package com.Udemy.YeoGiDa.domain.member.exception;

import com.Udemy.YeoGiDa.global.exception.ForbiddenException;

public class PasswordMismatchException extends ForbiddenException {
    public PasswordMismatchException() {
        super("비밀번호가 일치하지 않습니다.");
    }
}