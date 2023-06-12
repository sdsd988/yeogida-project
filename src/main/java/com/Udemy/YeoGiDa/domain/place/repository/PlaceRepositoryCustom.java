package com.Udemy.YeoGiDa.domain.place.repository;

import com.Udemy.YeoGiDa.domain.member.entity.Member;
import com.Udemy.YeoGiDa.domain.place.entity.Place;

import java.util.List;

public interface PlaceRepositoryCustom {

    List<Place> findAllByTripIdAndTagAndCondition(Long tripId, String tag, String condition);

    List<Place> findAllByComment(Member member);

    List<Place> findAllByTripId(Long tripId);

    List<Place> findAllByKeyword(String keyword);
}
