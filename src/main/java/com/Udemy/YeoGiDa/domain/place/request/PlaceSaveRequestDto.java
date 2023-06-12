package com.Udemy.YeoGiDa.domain.place.request;

import com.Udemy.YeoGiDa.domain.trip.entity.Trip;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.geo.Point;

import java.util.List;

@NoArgsConstructor
@Getter
@Setter
public class PlaceSaveRequestDto {

    private String title;
    private String address;
    private Double longitude;
    private Double latitude;
    private Float star;
    private String content;
    private String tag;

    @Builder
    public PlaceSaveRequestDto(String title, String address, Double longitude, Double latitude, Float star, String content, String tag) {
        this.title = title;
        this.address = address;
        this.longitude = longitude;
        this.latitude = latitude;
        this.star = star;
        this.content = content;
        this.tag = tag;
    }
}