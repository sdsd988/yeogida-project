package com.Udemy.YeoGiDa.domain.place.response;

import com.Udemy.YeoGiDa.domain.place.entity.Place;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PlaceAroundMarkerResponseDto {

    private Long tripId;

    private Long placeId;

    private String imgUrl;

    private String nickname;

    private float star;

    @Builder
    public PlaceAroundMarkerResponseDto(Place place) {
        this.tripId = place.getTrip().getId();
        this.placeId = place.getId();
        this.imgUrl = place.getPlaceImgs().get(0).getImgUrl();
        this.nickname = place.getTrip().getMember().getNickname();
        this.star = place.getStar();
    }
}
