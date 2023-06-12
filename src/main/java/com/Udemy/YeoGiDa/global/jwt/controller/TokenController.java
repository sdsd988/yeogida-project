package com.Udemy.YeoGiDa.global.jwt.controller;

import com.Udemy.YeoGiDa.global.jwt.service.JwtTokenProvider;
import com.Udemy.YeoGiDa.global.response.success.DefaultResult;
import com.Udemy.YeoGiDa.global.response.success.StatusCode;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/token")
public class TokenController {

    private final JwtTokenProvider jwtTokenProvider;

    @ApiOperation("토큰 유효성 검사")
    @GetMapping("/validate")
    public ResponseEntity validateToken(@RequestHeader HashMap<String, String> hashMap) {
        String oldAccessToken = hashMap.get("accesstoken");
        boolean isAccessTokenValid = false;
        isAccessTokenValid = jwtTokenProvider.validateToken(oldAccessToken);

        if(isAccessTokenValid == true) {
            log.info("accessToken 유효함 클라이언트에서 처리");

            return new ResponseEntity(DefaultResult.res(StatusCode.OK,
                    "유효한 accessToken 입니다."), HttpStatus.OK);
        } else {
            String refreshToken = hashMap.get("refreshtoken");
            String accessToken = jwtTokenProvider.validateRefreshTokenAndReissueAccessToken(refreshToken);
            Map<String, Object> result = new HashMap<>();
            result.put("newAccessToken", accessToken);
            result.put("refreshToken", refreshToken);

            return new ResponseEntity(DefaultResult.res(StatusCode.CREATED,
                    "새로운 accessToken 발행 성공", result), HttpStatus.CREATED);
        }
    }
}
