package com.Udemy.YeoGiDa.domain.heart.exception;

public class AlreadyHeartException extends IllegalArgumentException {

    public AlreadyHeartException() {
        super("이미 좋아요한 게시글입니다.");
    }
}
