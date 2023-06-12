package com.Udemy.YeoGiDa.domain.trip.repository;

import com.Udemy.YeoGiDa.domain.member.entity.Member;
import com.Udemy.YeoGiDa.domain.trip.entity.Trip;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TripRepository extends JpaRepository<Trip, Long>, TripRepositoryCustom {

    Optional<Trip> findById(Long tripId);

    @Query(value = "SELECT t FROM Trip t WHERE t.title LIKE %:keyword% OR t.subTitle LIKE %:keyword%")
    List<Trip> findAllSearch(@Param("keyword") String keyword);

    List<Trip> findAllByMember(Member member);

    List<Trip> findAllByMemberId(Long memberId);

    List<Trip> findByIdIn(List<Long> tripIds);
}
