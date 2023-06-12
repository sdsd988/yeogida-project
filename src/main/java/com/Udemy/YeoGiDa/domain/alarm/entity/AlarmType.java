package com.Udemy.YeoGiDa.domain.alarm.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum AlarmType {
    NEW_COMMENT("님이 내 장소에 댓글을 남겼어요!"),
    NEW_HEART("님이 내 여행지에 좋아요를 눌렀어요!"),
    NEW_FOLLOW("님이 나를 팔로우 했어요!")
    ;

    private final String alarmText;
}
