package com.Udemy.YeoGiDa.domain.member.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
public class MemberJoinRequest {

    @NotEmpty
    @Email
    private String email;

    @NotEmpty
    private String kakaoId;

    @NotEmpty
    private String nickname;
}
