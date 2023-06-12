package com.Udemy.YeoGiDa.domain.alarm.repository;

import com.Udemy.YeoGiDa.domain.alarm.entity.Alarm;
import com.Udemy.YeoGiDa.domain.alarm.entity.AlarmType;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.Udemy.YeoGiDa.domain.alarm.entity.QAlarm.alarm;

@Repository
@RequiredArgsConstructor
public class AlarmRepositoryImpl implements AlarmRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<Alarm> findFollowAlarmByMemberId(Long memberId) {
        return queryFactory.selectFrom(alarm)
                .where(alarm.alarmType.eq(AlarmType.NEW_FOLLOW))
                .where(alarm.followerId.eq(memberId))
                .fetch();
    }

    @Override
    public List<Alarm> findHeartAlarmByTripId(Long tripId) {
        return queryFactory.selectFrom(alarm)
                .where(alarm.alarmType.eq(AlarmType.NEW_HEART))
                .where(alarm.tripId.eq(tripId))
                .fetch();
    }

    @Override
    public List<Alarm> findCommentAlarmByPlaceId(Long placeId) {
        return queryFactory.selectFrom(alarm)
                .where(alarm.alarmType.eq(AlarmType.NEW_COMMENT))
                .where(alarm.placeId.eq(placeId))
                .fetch();
    }

    @Override
    public List<Alarm> findCommentAlarmByTripId(Long tripId) {
        return queryFactory.selectFrom(alarm)
                .where(alarm.alarmType.eq(AlarmType.NEW_COMMENT))
                .where(alarm.tripId.eq(tripId))
                .fetch();
    }
}
