package com.Udemy.YeoGiDa.domain.alarm.repository;

import com.Udemy.YeoGiDa.domain.alarm.entity.Alarm;
import com.Udemy.YeoGiDa.domain.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AlarmRepository extends JpaRepository<Alarm, Long>, AlarmRepositoryCustom {

    List<Alarm> findAllByMember(Member member);

    Optional<Alarm> findAlarmByTripIdAndMakeMemberId(Long tripId, Long makeMemberId);

    Optional<Alarm> findAlarmByMemberAndFollowerId(Member member, Long followerId);

    Optional<Alarm> findAlarmByCommentIdAndMakeMemberId(Long commentId, Long makeMemberId);
}
