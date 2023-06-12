package com.Udemy.YeoGiDa.domain.place.response;

import com.Udemy.YeoGiDa.domain.place.entity.Place;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@NoArgsConstructor
@Getter
@Setter
public class PlaceListInMapResponseDto {

    private Long placeId;
    private String title;
    private String address;
    private Float star;
    private Integer commentCount;
    private String imgUrl;
    private String tag;
    private Double latitude;
    private Double longitude;


    public PlaceListInMapResponseDto(Place place) {
        this.placeId=place.getId();
        this.title = place.getTitle();
        this.address=place.getAddress();
        this.latitude = place.getLatitude();
        this.longitude = place.getLongitude();
        this.star = place.getStar();
        this.commentCount = place.getComments().size();
        if(place.getPlaceImgs().size() > 0) {
            this.imgUrl = place.getPlaceImgs().get(0).getImgUrl();
        } else {
            this.imgUrl = null;
        }
        this.tag = place.getTag();
    }
}
