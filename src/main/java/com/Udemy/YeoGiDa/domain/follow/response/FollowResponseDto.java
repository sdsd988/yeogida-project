package com.Udemy.YeoGiDa.domain.follow.response;

import com.Udemy.YeoGiDa.domain.member.entity.Member;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class FollowResponseDto {

    private Long memberId;
    private String nickname;
    private String imgUrl;

    @Builder
    public FollowResponseDto(Member member) {
        this.memberId = member.getId();
        this.nickname = member.getNickname();
        if(member.getMemberImg() == null) {
            imgUrl = null;
        } else {
            this.imgUrl = member.getMemberImg().getImgUrl();
        }
    }
}
