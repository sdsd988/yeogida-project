package com.Udemy.YeoGiDa.domain.alarm.service;

import com.Udemy.YeoGiDa.domain.alarm.entity.Alarm;
import com.Udemy.YeoGiDa.domain.alarm.repository.AlarmRepository;
import com.Udemy.YeoGiDa.domain.alarm.response.AlarmListResponseDto;
import com.Udemy.YeoGiDa.domain.member.entity.Member;
import com.Udemy.YeoGiDa.domain.member.exception.MemberNotFoundException;
import com.Udemy.YeoGiDa.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class AlarmService {

    private final AlarmRepository alarmRepository;
    private final MemberRepository memberRepository;

    public List<AlarmListResponseDto> alarmList(Member member) {
        if(member == null) {
            throw new MemberNotFoundException();
        }

        List<Alarm> alarms = alarmRepository.findAllByMember(member);
        List<AlarmListResponseDto> alarmListResponseDtos = new ArrayList<>();
        for (Alarm alarm : alarms) {
            Long makeAlarmMemberId = alarm.getMakeMemberId();
            Optional<Member> makeMember = memberRepository.findById(makeAlarmMemberId);
            makeMember.ifPresent(value -> alarmListResponseDtos.add(new AlarmListResponseDto(alarm, value)));
        }
        Collections.reverse(alarmListResponseDtos);
        return alarmListResponseDtos;
    }

    @Transactional
    public void deleteAlarmsAfterOneWeek() {
        List<Alarm> all = alarmRepository.findAll();
        LocalDateTime now = LocalDateTime.now();
        for (Alarm alarm : all) {
            LocalDateTime createdTime = alarm.getCreatedTime();
            LocalDateTime plusDays = createdTime.plusDays(7);
            log.info("plusDays={}", plusDays);
            log.info("plusDays.isBeforeNow={}", plusDays.isBefore(now));
            if(plusDays.isBefore(now)) {
                log.info("plusDays={}", plusDays);
                log.info("###들어옴");
                alarmRepository.delete(alarm);
            }
        }
    }
}
