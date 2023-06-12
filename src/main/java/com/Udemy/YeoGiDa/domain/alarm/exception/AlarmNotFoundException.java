package com.Udemy.YeoGiDa.domain.alarm.exception;

import com.Udemy.YeoGiDa.global.exception.NotFoundException;

public class AlarmNotFoundException extends NotFoundException {

    public AlarmNotFoundException() {
        super("알람을 찾을 수 없습니다.");
    }
}
