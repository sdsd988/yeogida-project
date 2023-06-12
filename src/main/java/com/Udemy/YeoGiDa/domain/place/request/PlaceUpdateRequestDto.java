package com.Udemy.YeoGiDa.domain.place.request;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class PlaceUpdateRequestDto {

    private String content;
    private Float star;
    private String tag;

    @Builder
    public PlaceUpdateRequestDto(String title, String address,  Double longitude, Double latitude, String content, Float star, String tag) {
        this.content = content;
        this.star = star;
        this.tag = tag;
    }
}