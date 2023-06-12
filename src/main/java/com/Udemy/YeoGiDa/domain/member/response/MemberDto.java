package com.Udemy.YeoGiDa.domain.member.response;

import com.Udemy.YeoGiDa.domain.member.entity.Member;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter @Setter
public class MemberDto {

    private Long id;
    private String email;
    private String nickname;
    private String imgUrl;

    @Builder
    public MemberDto(Member member) {
        this.id = member.getId();
        this.email = member.getEmail();
        this.nickname = member.getNickname();
        if(member.getMemberImg() == null) {
            imgUrl = null;
        } else {
            this.imgUrl = member.getMemberImg().getImgUrl();
        }
    }
}
