package com.Udemy.YeoGiDa.domain.heart.repository;

import com.Udemy.YeoGiDa.domain.heart.entity.Heart;
import com.Udemy.YeoGiDa.domain.member.entity.Member;
import com.Udemy.YeoGiDa.domain.trip.entity.Trip;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface HeartRepository extends JpaRepository<Heart, Long>, HeartRepositoryCustom {

    Optional<Heart> findByMemberAndTrip(Member member, Trip trip);

    List<Heart> findAllByTripId(Long tripId);

    boolean existsByTripIdAndMember(Long tripId, Member member);

    List<Trip> findByIdIn(List<Long> tripIds);

    List<Heart> findAllByMember(Member member);
}
