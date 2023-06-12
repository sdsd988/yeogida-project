package com.Udemy.YeoGiDa.domain.alarm.repository;

import com.Udemy.YeoGiDa.domain.alarm.entity.Alarm;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AlarmRepositoryCustom {

    List<Alarm> findFollowAlarmByMemberId(Long memberId);

    List<Alarm> findHeartAlarmByTripId(Long tripId);

    List<Alarm> findCommentAlarmByPlaceId(Long placeId);

    List<Alarm> findCommentAlarmByTripId(Long tripId);
}
