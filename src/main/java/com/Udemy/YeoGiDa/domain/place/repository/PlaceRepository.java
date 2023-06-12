package com.Udemy.YeoGiDa.domain.place.repository;

import com.Udemy.YeoGiDa.domain.place.entity.Place;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PlaceRepository extends JpaRepository<Place,Long>, PlaceRepositoryCustom {

    List<Place> findAll();

    List<Place> findAllByLatitudeAndLongitude(Double latitude, Double longitude);

    List<Place> findAllByTripId(Long tripId);

    List<Place> findByIdIn(List<Long> placeIds);
}

