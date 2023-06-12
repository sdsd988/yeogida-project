package com.Udemy.YeoGiDa.domain.trip.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class TripRankResponseDto {

    private String searchKeyword;
    private Double score;

    public TripRankResponseDto(String searchKeyword, Double score) {
        this.searchKeyword = searchKeyword;
        this.score = score;
    }
}
