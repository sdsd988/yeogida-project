package com.Udemy.YeoGiDa.domain.follow.exception;

import com.Udemy.YeoGiDa.global.exception.NotFoundException;

public class NoOneFollowException extends NotFoundException {

    public NoOneFollowException() {
        super("팔로우한 사람이 아무도 없습니다.");
    }

}
