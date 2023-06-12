package com.Udemy.YeoGiDa.domain.trip.repository;

import com.Udemy.YeoGiDa.domain.trip.entity.Trip;
import com.Udemy.YeoGiDa.domain.trip.entity.TripImg;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TripImgRepository extends JpaRepository<TripImg, Long> {
    TripImg findTripImgByTrip(Trip trip);
}
