package com.Udemy.YeoGiDa.domain.alarm.controller;

import com.Udemy.YeoGiDa.domain.alarm.response.AlarmListResponseDto;
import com.Udemy.YeoGiDa.domain.alarm.service.AlarmService;
import com.Udemy.YeoGiDa.domain.member.entity.Member;
import com.Udemy.YeoGiDa.global.response.success.DefaultResult;
import com.Udemy.YeoGiDa.global.response.success.StatusCode;
import com.Udemy.YeoGiDa.global.security.annotation.LoginMember;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/me")
@Slf4j
public class AlarmController {

    private final AlarmService alarmService;

    @ApiOperation("알람 목록 조회")
    @GetMapping("/alarm")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity getAlarmList(@LoginMember Member member) {
        log.info("getAlarmList 들어옴");
        log.info("member={}", member);
        log.info("member Nickname={}", member.getNickname());
        List<AlarmListResponseDto> alarmListResponseDtos = alarmService.alarmList(member);
        Map<String, Object> result = new HashMap<>();
        result.put("alarmList", alarmListResponseDtos);
        return new ResponseEntity(DefaultResult.res(StatusCode.OK,
                "알람 목록 조회 성공", result), HttpStatus.OK);

        //response object to json
        //jackson
    }
}
