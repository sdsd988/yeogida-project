package com.Udemy.YeoGiDa.domain.place.exception;

import com.Udemy.YeoGiDa.global.exception.NotFoundException;

public class PlaceNotFoundException extends NotFoundException {
    public PlaceNotFoundException() {
        super("장소를 찾을 수 없습니다.");
    }
}
