package com.Udemy.YeoGiDa.global.fcm.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class RequestDto {
    private String targetToken;
    private String title;
    private String body;
    private String alarmType;
    private String targetId;
}
