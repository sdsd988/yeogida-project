package com.Udemy.YeoGiDa.domain.place.repository;

import com.Udemy.YeoGiDa.domain.place.entity.Place;
import com.Udemy.YeoGiDa.domain.place.entity.PlaceImg;
import com.Udemy.YeoGiDa.domain.place.entity.QPlace;
import com.Udemy.YeoGiDa.domain.place.entity.QPlaceImg;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;

import static com.Udemy.YeoGiDa.domain.place.entity.QPlaceImg.*;

@RequiredArgsConstructor
public class PlaceImgRepositoryImpl implements PlaceImgRepositoryCustom{

    private final JPAQueryFactory queryFactory;

    @Override
    public List<PlaceImg> findPlaceImgsByPlaceFetch(Place place) {
        return queryFactory.selectFrom(placeImg)
                .leftJoin(placeImg.place, QPlace.place).fetchJoin()
                .where(placeImg.place.id.eq(place.getId()))
                .fetch();
    }
}
