package com.Udemy.YeoGiDa.domain.member.exception;

public class MemberDuplicateException extends IllegalArgumentException {

    public MemberDuplicateException() {
        super("중복된 회원입니다.");
    }
}
