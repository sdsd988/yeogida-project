package com.Udemy.YeoGiDa.domain.follow.exception;

import com.Udemy.YeoGiDa.global.exception.NotFoundException;

public class FollowNotFoundException extends NotFoundException {

    public FollowNotFoundException() {
        super("팔로우 하지 않은 멤버입니다.");
    }

}
