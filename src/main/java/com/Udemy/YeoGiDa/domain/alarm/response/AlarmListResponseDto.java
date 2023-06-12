package com.Udemy.YeoGiDa.domain.alarm.response;

import com.Udemy.YeoGiDa.domain.alarm.entity.Alarm;
import com.Udemy.YeoGiDa.domain.alarm.entity.AlarmType;
import com.Udemy.YeoGiDa.domain.member.entity.Member;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class AlarmListResponseDto {

    private String nickname;

    private String imgUrl;

    private AlarmType alarmType;

    private Long followerId;

    private Long tripId;

    private Long placeId;

    private Long commentId;

    private String text;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime createdTime;

    @Builder
    public AlarmListResponseDto(Alarm alarm, Member member) {
        this.nickname = member.getNickname();
        this.imgUrl = member.getMemberImg().getImgUrl();
        this.alarmType = alarm.getAlarmType();
        this.followerId = alarm.getFollowerId();
        this.tripId = alarm.getTripId();
        this.placeId = alarm.getPlaceId();
        this.commentId = alarm.getCommentId();
        this.text = alarm.getAlarmType().getAlarmText();
        this.createdTime = alarm.getCreatedTime();
    }
}
