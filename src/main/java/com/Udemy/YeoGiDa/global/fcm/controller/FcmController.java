package com.Udemy.YeoGiDa.global.fcm.controller;

import com.Udemy.YeoGiDa.global.fcm.dto.RequestDto;
import com.Udemy.YeoGiDa.global.fcm.service.FirebaseCloudMessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
public class FcmController {

    private final FirebaseCloudMessageService firebaseCloudMessageService;

    @PostMapping("/api/v1/fcm")
    public ResponseEntity pushMessage(@RequestBody RequestDto requestDto) throws IOException {
        firebaseCloudMessageService.sendMessageTo(
                requestDto.getTargetToken(),
                requestDto.getTitle(),
                requestDto.getBody(),
                requestDto.getAlarmType(),
                requestDto.getTargetId());
        return ResponseEntity.ok().build();
    }

}
