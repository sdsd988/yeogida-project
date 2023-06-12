package com.Udemy.YeoGiDa.domain.follow.exception;

public class AlreadyFollowException extends IllegalArgumentException {
    public AlreadyFollowException() {
        super("이미 좋아요한 게시글입니다.");
    }
}
