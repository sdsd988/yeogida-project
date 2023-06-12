package com.Udemy.YeoGiDa.domain.place.response;

import com.Udemy.YeoGiDa.domain.place.entity.Place;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PlaceAroundViewResponseDto {

    private Long placeId;

    private String title;

    private String address;

    private Double latitude;

    private Double longitude;

    @Builder
    public PlaceAroundViewResponseDto(Place place) {
        this.placeId = place.getId();
        this.title = place.getTitle();
        this.address = place.getAddress();
        this.latitude = place.getLatitude();
        this.longitude = place.getLongitude();
    }
}
