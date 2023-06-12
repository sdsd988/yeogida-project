package com.Udemy.YeoGiDa.domain.trip.repository;

import com.Udemy.YeoGiDa.domain.member.entity.Member;
import com.Udemy.YeoGiDa.domain.trip.entity.Trip;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TripRepositoryCustom {

    //모든 여행지 목록 최신순 - 페이징 필요
    List<Trip> findAllByConditionFetch(String condition);

    //지역별 여행지 목록 - 최신순 정렬 - 페이징 필요
    List<Trip> findAllByRegionAndConditionFetch(String region, String Condition);

    //월간 베스트 여행지 목록 기본 10개
    List<Trip> findAllOrderByChangeHeartCountBasicFetch();

    //월간 베스트 여행지 목록 더 보기 - 페이징 필요
    List<Trip> findAllOrderByChangeHeartCountMoreFetch();

    //내가 작성한 여행지 목록
    List<Trip> findAllByMemberFetch(Member m);
    //유저가 작성한 여행지 목록, 지역 조건, 정렬
    List<Trip> findAllByMemberIdFetch(Long memberId,String region,String condition);

    //전체 여행지 검색, 정렬
    List<Trip> findAllTripSearchAndSort(String keyword, String condition);

    //유저가 작성한 여행지 목록 전체, 정렬
    List<Trip> findAllByMemberIdFetch2(Long memberId,String condition);

    //친구의 최근 여행지 목록 기본 10개
    List<Trip> findAllByFollowingOrderByIdBasicFetch(Member m);

    //친구의 최근 여행지 목록 전부
    List<Trip> findAllByFollowingOrderByIdMoreFetch(Member m);

    //최근 여행지 10개
    List<Trip> findRecentTrip();
}
