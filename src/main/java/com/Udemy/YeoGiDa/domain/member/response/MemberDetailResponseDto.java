package com.Udemy.YeoGiDa.domain.member.response;

import com.Udemy.YeoGiDa.domain.member.entity.Member;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class MemberDetailResponseDto {

    private Long id;
    private String nickname;
    private String imgUrl;
    private Integer followerCount;
    private Integer followingCount;
    private Integer tripCount;
    private Integer heartCount;

    public MemberDetailResponseDto(Member member) {
        this.id = member.getId();
        this.nickname = member.getNickname();
        if(member.getMemberImg() == null) {
            imgUrl = null;
        } else {
            this.imgUrl = member.getMemberImg().getImgUrl();
        }
        this.followerCount = null;
        this.followingCount = null;
        this.tripCount = member.getTrips().size();
        this.heartCount= member.getHeartCount();
    }


}
