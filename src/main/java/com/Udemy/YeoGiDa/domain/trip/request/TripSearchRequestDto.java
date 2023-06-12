package com.Udemy.YeoGiDa.domain.trip.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class TripSearchRequestDto {

    private String keyword;
    private String condition;
}
