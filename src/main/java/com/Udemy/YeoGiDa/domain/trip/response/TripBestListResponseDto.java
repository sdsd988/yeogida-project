package com.Udemy.YeoGiDa.domain.trip.response;

import com.Udemy.YeoGiDa.domain.trip.entity.Trip;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class TripBestListResponseDto {

    private Long tripId;
    private Long memberId;
    private String title;
    private String imgUrl;
    private String nickname;

    public TripBestListResponseDto(Trip trip) {
        this.tripId = trip.getId();
        this.memberId = trip.getMember().getId();
        this.title = trip.getTitle();
        if(trip.getTripImg() == null) {
            this.imgUrl = null;
        } else {
            this.imgUrl = trip.getTripImg().getImgUrl();
        }
        this.nickname = trip.getMember().getNickname();
    }
}
