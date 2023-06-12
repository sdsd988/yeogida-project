package com.Udemy.YeoGiDa.domain.place.repository;

import com.Udemy.YeoGiDa.domain.place.entity.Place;
import com.Udemy.YeoGiDa.domain.place.entity.PlaceImg;

import java.util.List;

public interface PlaceImgRepositoryCustom {

    List<PlaceImg> findPlaceImgsByPlaceFetch(Place place);
}
