package com.Udemy.YeoGiDa.domain.member.exception;

public class AlreadyExistsNicknameException extends IllegalArgumentException {

    public AlreadyExistsNicknameException() {
        super("이미 존재하는 닉네임입니다.");
    }
}
