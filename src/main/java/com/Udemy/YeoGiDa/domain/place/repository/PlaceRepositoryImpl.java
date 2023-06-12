package com.Udemy.YeoGiDa.domain.place.repository;

import com.Udemy.YeoGiDa.domain.member.entity.Member;
import com.Udemy.YeoGiDa.domain.place.entity.Place;
import com.Udemy.YeoGiDa.domain.trip.entity.QTripImg;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.util.StringUtils;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;

import static com.Udemy.YeoGiDa.domain.comment.entity.QComment.comment;
import static com.Udemy.YeoGiDa.domain.place.entity.QPlace.*;
import static com.Udemy.YeoGiDa.domain.trip.entity.QTrip.*;

@RequiredArgsConstructor
public class PlaceRepositoryImpl implements PlaceRepositoryCustom{

    private final JPAQueryFactory queryFactory;

    @Override
    public List<Place> findAllByTripIdAndTagAndCondition(Long tripId, String tag, String condition) {
        return queryFactory.selectFrom(place)
                .leftJoin(place.trip, trip).fetchJoin()
                .leftJoin(trip.tripImg, QTripImg.tripImg).fetchJoin()
                .where(place.trip.id.eq(tripId),whereCondition(tag))
                .orderBy(conditionParam(condition))
                .fetch();
    }

    @Override
    public List<Place> findAllByComment(Member member) {
        return queryFactory.selectFrom(place).distinct()
                .leftJoin(place.comments,comment).fetchJoin()
                .where(comment.member.id.eq(member.getId()))
                .orderBy(comment.id.desc())
                .fetch();
    }

    private BooleanExpression whereCondition(String tag) {
        if(tag.equals("nothing")){
            return null;
        } return place.tag.eq(tag);
    }

    private OrderSpecifier conditionParam(String condition) {
        if (StringUtils.isNullOrEmpty(condition)) {
            return place.id.asc();
        } else if (condition.equals("id")) {
            return place.id.desc();
        } else if (condition.equals("star")) {
            return place.star.desc();
        } else if (condition.equals("comment")) {
            return place.comments.size().desc();
        } throw new IllegalArgumentException();
    }



    @Override
    public List<Place> findAllByTripId(Long tripId) {
        return queryFactory.selectFrom(place)
                .where(place.trip.id.eq(tripId))
                .orderBy(place.id.desc())
                .fetch();
    }

    @Override
    public List<Place> findAllByKeyword(String keyword) {
        return queryFactory.selectFrom(place)
                .where(place.address.contains(keyword).or(place.title.contains(keyword)))
                .orderBy(place.id.desc())
                .fetch();
    }
}
