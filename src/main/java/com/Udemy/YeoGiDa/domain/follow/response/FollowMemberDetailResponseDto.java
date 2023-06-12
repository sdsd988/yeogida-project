package com.Udemy.YeoGiDa.domain.follow.response;

import com.Udemy.YeoGiDa.domain.member.entity.Member;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class FollowMemberDetailResponseDto {

        private Long memberId;
        private String nickname;
        private String imgUrl;
        private Integer followerCount;
        private Integer followingCount;
        private Integer tripCount;
        private Integer heartCount;
        private Boolean isFollow;


        public FollowMemberDetailResponseDto(Member member) {
            this.memberId = member.getId();
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
            this.isFollow = null;
        }


}

