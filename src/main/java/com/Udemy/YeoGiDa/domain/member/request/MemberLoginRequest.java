package com.Udemy.YeoGiDa.domain.member.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MemberLoginRequest {

    @NotEmpty
    @Email
    private String email;

    @NotEmpty
    private String kakaoId;

    @NotEmpty
    private String deviceToken;
}
