package com.Udemy.YeoGiDa.domain.heart.exception;

import com.Udemy.YeoGiDa.global.exception.NotFoundException;

public class HeartNotFoundException extends NotFoundException {

    public HeartNotFoundException() {
        super("좋아요 누르지 않은 글입니다.");
    }
}
