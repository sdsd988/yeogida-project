package com.Udemy.YeoGiDa.domain.trip.exception;

import com.Udemy.YeoGiDa.global.exception.NotFoundException;

public class HeartTripNotFoundException extends NotFoundException {

    public HeartTripNotFoundException() {
        super("좋아요한 여행지가 없습니다.");
    }
}
